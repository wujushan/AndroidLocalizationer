/*
 * Copyright 2014-2015 Wesley Lin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.wujs.data.task;

import com.example.wujs.action.AndroidLocalization;
import com.example.wujs.language_engine.TranslationEngineType;
import com.example.wujs.language_engine.baidu.BaiduTranslationApi;
import com.example.wujs.language_engine.google.GoogleTranslationApi;
import com.example.wujs.module.AndroidString;
import com.example.wujs.module.FilterRule;
import com.example.wujs.module.SupportedLanguages;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.example.wujs.data.Log;
import com.example.wujs.data.SerializeUtil;
import com.example.wujs.data.StorageDataKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wesley Lin on 12/1/14.
 */
public class GetTranslationTask extends Task.Backgroundable {

    private List<SupportedLanguages> selectedLanguages;
    private final List<AndroidString> androidStrings;
    private double indicatorFractionFrame;
    private TranslationEngineType translationEngineType;
    private boolean override;
    private VirtualFile clickedFile;

    private static final String BingIdInvalid = "Invalid client id or client secret, " +
            "please check them <html><a href=\"https://datamarket.azure.com/developer/applications\">here</a></html>";
    private static final String BingQuotaExceeded = "Microsoft Translator quota exceeded, " +
            "please check your data usage <html><a href=\"https://datamarket.azure.com/account/datasets\">here</a></html>";

    private static final String GoogleErrorUnknown = "Error, please check API key in the settings panel.";
    private static final String GoogleDailyLimitError = "Daily Limit Exceeded, please note that Google Translation API " +
            "is a <html><a href=\"https://cloud.google.com/translate/v2/pricing\">paid service.</a></html>";

    private String errorMsg = null;

    public GetTranslationTask(Project project, String title,
                              List<SupportedLanguages> selectedLanguages,
                              List<AndroidString> androidStrings,
                              TranslationEngineType translationEngineType,
                              boolean override,
                              VirtualFile clickedFile) {
        super(project, title);
        this.selectedLanguages = selectedLanguages;
        this.androidStrings = androidStrings;
        this.translationEngineType = translationEngineType;
        this.indicatorFractionFrame = 1.0d / (double) (this.selectedLanguages.size());
        this.override = override;
        this.clickedFile = clickedFile;
    }

    @Override
    public void run(ProgressIndicator indicator) {
        for (int i = 0; i < selectedLanguages.size(); i++) {

            SupportedLanguages language = selectedLanguages.get(i);

            if (language != null && !"".equals(language) && !language.equals(SupportedLanguages.English)) {

                List<AndroidString> androidStringList = filterAndroidString(androidStrings, language, override);

                List<List<AndroidString>> filteredAndSplittedString
                        = splitAndroidString(androidStringList, translationEngineType);

                List<AndroidString> translationResult = new ArrayList<AndroidString>();
                for (int j = 0; j < filteredAndSplittedString.size(); j++) {

                    List<AndroidString> strings = getTranslationEngineResult(
                            filteredAndSplittedString.get(j),
                            language,
                            SupportedLanguages.AUTO_BAIDU,
                            translationEngineType
                    );

                    if (strings == null) {
                        Log.i("language===" + language);
                        continue;
                    }
                    translationResult.addAll(strings);
                    indicator.setFraction(indicatorFractionFrame * (double) (i)
                            + indicatorFractionFrame / filteredAndSplittedString.size() * (double) (j));
                    indicator.setText("Translating to " + language.getLanguageEnglishDisplayName()
                            + " (" + language.getLanguageDisplayName() + ")");
                }
                String fileName = getValueResourcePath(language);
                List<AndroidString> fileContent = getTargetAndroidStrings(androidStrings, translationResult, fileName, override);
                writeAndroidStringToLocal(myProject, fileName, fileContent);
            }
        }
    }


    @Override
    public void onSuccess() {

        if (errorMsg == null || errorMsg.isEmpty())
            return;
        AndroidLocalization.showSuccessDialog(getProject(), "translation Success");
    }

    private String getValueResourcePath(SupportedLanguages language) {
        String resPath = clickedFile.getPath().substring(0,
                clickedFile.getPath().indexOf("/res/") + "/res/".length());

        return resPath + "values-" + language.getAndroidStringFolderNameSuffix()
                + "/" + clickedFile.getName();
    }

    // todo: if got error message, should break the background task
    private List<AndroidString> getTranslationEngineResult(@NotNull List<AndroidString> needToTranslatedString,
                                                           @NotNull SupportedLanguages targetLanguageCode,
                                                           @NotNull SupportedLanguages sourceLanguageCode,
                                                           TranslationEngineType translationEngineType) {

        List<String> querys = AndroidString.getAndroidStringValues(needToTranslatedString);
        Log.i(querys.toString());

        List<String> result = null;

        switch (translationEngineType) {
            case Baidu:
                result = BaiduTranslationApi
                        .getTranslationJSON(querys,targetLanguageCode,sourceLanguageCode);
                break;
            case Bing:
                break;
            case Google:
                result = GoogleTranslationApi
                        .getTranslationJSON(querys, targetLanguageCode, sourceLanguageCode);
                if (result == null) {
                    errorMsg = GoogleErrorUnknown;
                    return null;
                } else if (result.isEmpty() && !querys.isEmpty()) {
                    errorMsg = GoogleDailyLimitError;
                    return null;
                }
                break;
        }
        if (result == null || result.size() <= 0){
            return null;
        }
        List<AndroidString> translatedAndroidStrings = new ArrayList<>();
//        Logger.error(needToTranslatedString.size());
//        Logger.info("needToTranslatedString.size(): " + needToTranslatedString.size()+
//                "result.size(): " + result.size());
        for (int i = 0; i < needToTranslatedString.size(); i++) {
            translatedAndroidStrings.add(new AndroidString(
                    needToTranslatedString.get(i).getKey(), result.get(i)));
        }
        return translatedAndroidStrings;
    }

    private List<List<AndroidString>> splitAndroidString(List<AndroidString> origin, TranslationEngineType engineType) {

        List<List<AndroidString>> splited = new ArrayList<List<AndroidString>>();
        int splitFragment = 50;
        switch (engineType) {
            case Baidu:
                splitFragment = 50;
                break;
            case Bing:
                splitFragment = 50;
                break;
            case Google:
                splitFragment = 50;
                break;
        }

        if (origin != null && origin.size() > 0) {
            if (origin.size() <= splitFragment) {
                splited.add(origin);
            } else {
                int count = (origin.size() % splitFragment == 0) ? (origin.size() / splitFragment) : (origin.size() / splitFragment + 1);
                for (int i = 1; i <= count; i++) {
                    int end = i * splitFragment;
                    if (end > origin.size()) {
                        end = origin.size();
                    }

                    splited.add(origin.subList((i - 1) * splitFragment, end));
                }
            }
        }

        return splited;
    }

    private List<AndroidString> filterAndroidString(List<AndroidString> origin,
                                                    SupportedLanguages language,
                                                    boolean override) {
        List<AndroidString> result = new ArrayList<AndroidString>();


        VirtualFile targetStringFile = LocalFileSystem.getInstance().findFileByPath(
                getValueResourcePath(language));
        List<AndroidString> targetAndroidStrings = new ArrayList<AndroidString>();
        if (targetStringFile != null) {
            try {
                targetAndroidStrings = AndroidString.getAndroidStringsList(targetStringFile.contentsToByteArray());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        String rulesString = PropertiesComponent.getInstance().getValue(StorageDataKey.SettingFilterRules);
        List<FilterRule> filterRules = new ArrayList<FilterRule>();
        if (rulesString == null) {
            filterRules.add(FilterRule.DefaultFilterRule);
        } else {
            filterRules = SerializeUtil.deserializeFilterRuleList(rulesString);
        }
//        Log.i("targetAndroidString: " + targetAndroidStrings.toString());
        for (AndroidString androidString : origin) {
            // filter rules
            if (FilterRule.inFilterRule(androidString.getKey(), filterRules))
                continue;

            // override
            /*if (!override && !targetAndroidStrings.isEmpty()) {
                // check if there is the androidString in this file
                // if there is, filter it
                if (isAndroidStringListContainsKey(targetAndroidStrings, androidString.getKey())) {
                    continue;
                }
            }*/

            result.add(androidString);
        }

        return result;
    }

    private static List<AndroidString> getTargetAndroidStrings(List<AndroidString> sourceAndroidStrings,
                                                               List<AndroidString> translatedAndroidStrings,
                                                               String fileName,
                                                               boolean override) {

        if (translatedAndroidStrings == null) {
            translatedAndroidStrings = new ArrayList<AndroidString>();
        }

        VirtualFile existenceFile = LocalFileSystem.getInstance().findFileByPath(fileName);
        List<AndroidString> existenceAndroidStrings = null;
        if (existenceFile != null && !override) {
            try {
//                existenceAndroidStrings = AndroidString.getAndroidStringsList(existenceFile.contentsToByteArray());
                existenceAndroidStrings = AndroidString.getAndroidStrings(existenceFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            existenceAndroidStrings = new ArrayList<AndroidString>();
        }

        Log.i("sourceAndroidStrings: " + sourceAndroidStrings,
                "translatedAndroidStrings: " + translatedAndroidStrings,
                "existenceAndroidStrings: " + existenceAndroidStrings);

        List<AndroidString> targetAndroidStrings = new ArrayList<AndroidString>();

        for (int i = 0; i < sourceAndroidStrings.size(); i++) {
            AndroidString string = sourceAndroidStrings.get(i);
            AndroidString resultString = new AndroidString(string);

            // if override is checked, skip setting the existence value, for performance issue
            if (!override) {
                String existenceValue = getAndroidStringValueInList(existenceAndroidStrings, resultString.getKey());
                if (existenceValue != null) {
                    resultString.setValue(existenceValue);
                }
            }

            String translatedValue = getAndroidStringValueInList(translatedAndroidStrings, resultString.getKey());
            if (translatedValue != null) {
                resultString.setValue(translatedValue);
            }

            targetAndroidStrings.add(resultString);
        }
        Log.i("targetAndroidStrings: " + targetAndroidStrings);
        return targetAndroidStrings;
    }

    private static void writeAndroidStringToLocal(final Project myProject, String filePath, List<AndroidString> fileContent) {
        File file = new File(filePath);
        final VirtualFile virtualFile;
        boolean fileExits = true;
        try {
            file.getParentFile().mkdirs();
            if (!file.exists()) {
                fileExits = false;
                file.createNewFile();
            }
            //Change by GodLikeThomas FIX: Appeared Messy code under windows --start; 
            //FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            //BufferedWriter writer = new BufferedWriter(fileWriter);
            //writer.write(getFileContent(fileContent));
            //writer.close();
            FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            osw.write(getFileContent(fileContent));
            osw.close();
            //Change by GodLikeThomas FIX: Appeared Messy code under windows --end;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fileExits) {
            virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
            if (virtualFile == null)
                return;
            virtualFile.refresh(true, false, new Runnable() {
                @Override
                public void run() {
                    openFileInEditor(myProject, virtualFile);
                }
            });
        } else {
            virtualFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file);
            openFileInEditor(myProject, virtualFile);
        }
    }

    private static void openFileInEditor(final Project myProject, @Nullable final VirtualFile file) {
        if (file == null)
            return;

        // run in UI thread:
        //    https://theantlrguy.atlassian.net/wiki/display/~admin/Intellij+plugin+development+notes#Intellijplugindevelopmentnotes-GUIandthreads,backgroundtasks
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                final FileEditorManager editorManager = FileEditorManager.getInstance(myProject);
                editorManager.openFile(file, true);
            }
        });
    }

    private static String getFileContent(List<AndroidString> fileContent) {
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
        String stringResourceHeader = "<resources>\n\n";
        String stringResourceTail = "</resources>\n";

        StringBuilder sb = new StringBuilder();
        sb.append(xmlHeader).append(stringResourceHeader);
        for (AndroidString androidString : fileContent) {
            sb.append("\t").append(androidString.toString()).append("\n");
        }
        sb.append("\n").append(stringResourceTail);
        return sb.toString();
    }

    private static boolean isAndroidStringListContainsKey(List<AndroidString> androidStrings, String key) {
        List<String> keys = AndroidString.getAndroidStringKeys(androidStrings);
        return keys.contains(key);
    }

    public static String getAndroidStringValueInList(List<AndroidString> androidStrings, String key) {
        for (AndroidString androidString : androidStrings) {
            if (androidString.getKey().equals(key)) {
                return androidString.getValue();
            }
        }
        return null;
    }

}
