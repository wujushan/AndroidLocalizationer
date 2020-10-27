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

package com.example.wujs.module;


import com.example.wujs.language_engine.TranslationEngineType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wesley Lin on 11/29/14.
 */
public enum SupportedLanguages {
    Afrikaans("af", "Afrikaans", "Afrikaans"),
    Albanian("sq", "Shqiptar", "Albanian"),
    Arabic("ar", "العربية", "Arabic"),
    Azerbaijani("az", "Azərbaycan", "Azerbaijani"),
    Basque("eu", "Euskal", "Basque"),
    Bengali("bn", "বাঙালি", "Bengali"),
    Belarusian("be", "Беларускі", "Belarusian"),
    Bulgarian("bg", "Български", "Bulgarian"),
    Catalan("ca", "Català", "Catalan"),
    Chinese_Simplified("zh-CN", "简体中文", "Chinese Simplified"),
    Chinese_Simplified_BING("zh-CHS", "简体中文", "Chinese Simplified"),
    Chinese_Traditional("zh-TW", "正體中文", "Chinese Traditional"),
    Chinese_Traditional_BING("zh-CHT", "正體中文", "Chinese Traditional"),
    Croatian("hr", "Hrvatski", "Croatian"),
    Czech("cs", "Čeština", "Czech"),
    Danish("da", "Dansk", "Danish"),
    Dutch("nl", "Nederlands", "Dutch"),
    English("en", "English", "English"),
    Esperanto("eo", "Esperanta", "Esperanto"),
    Estonian("et", "Eesti", "Estonian"),
    Filipino("tl", "Pilipino", "Filipino"),
    Finnish("fi", "Suomi", "Finnish"),
    French("fr", "Français", "French"),
    Galician("gl", "Galego", "Galician"),
    Georgian("ka", "ქართული", "Georgian"),
    German("de", "Deutsch", "German"),
    Greek("el", "Ελληνικά", "Greek"),
    Gujarati("gu", "ગુજરાતી", "Gujarati"),
    Haitian_Creole("ht", "Haitiancreole", "Haitian Creole"),
    Hebrew("iw", "עברית", "Hebrew"),
    Hebrew_BING("he", "עברית", "Hebrew"),
    Hindi("hi", "हिंदी", "Hindi"),
    Hungarian("hu", "Magyar", "Hungarian"),
    Icelandic("is", "Icelandic", "Icelandic"),
    Indonesian("id", "Indonesia", "Indonesian"),
    Irish("ga", "Irish", "Irish"),
    Italian("it", "Italiano", "Italian"),
    Japanese("ja", "日本語", "Japanese"),
    Kannada("kn", "ಕನ್ನಡ", "Kannada"),
    Korean("ko", "한국의", "Korean"),
    Latin("la", "Latina", "Latin"),
    Latvian("lv", "Latvijas", "Latvian"),
    Lithuanian("lt", "Lietuvos", "Lithuanian"),
    Macedonian("mk", "Македонски", "Macedonian"),
    Malay("ms", "Melayu", "Malay"),
    Maltese("mt", "Malti", "Maltese"),
    Norwegian("no", "Norsk", "Norwegian"),
    Persian("fa", "فارسی", "Persian"),
    Polish("pl", "Polski", "Polish"),
    Portuguese("pt", "Português", "Portuguese"),
    Romanian("ro", "Român", "Romanian"),
    Russian("ru", "Русский", "Russian"),
    Serbian("sr", "Српски", "Serbian"),
    Slovak("sk", "Slovenčina", "Slovak"),
    Slovenian("sl", "Slovenščina", "Slovenian"),
    Spanish("es", "Español", "Spanish"),
    Swahili("sw", "Kiswahili", "Swahili"),
    Swedish("sv", "Svenska", "Swedish"),
    Tamil("ta", "தமிழ்", "Tamil"),
    Telugu("te", "తెలుగు", "Telugu"),
    Thai("th", "ไทย", "Thai"),
    Turkish("tr", "Türk", "Turkish"),
    Ukrainian("uk", "Український", "Ukrainian"),
    Urdu("ur", "اردو", "Urdu"),
    Vietnamese("vi", "Tiếng Việt", "Vietnamese"),
    Welsh("cy", "Cymraeg", "Welsh"),
    Yiddish("yi", "ייִדיש", "Yiddish"),

    AUTO_BAIDU("auto","自动检测","auto check"),
    Chinese_Simplified_BAIDU("zh", "简体中文", "Chinese Simplified","zh-rCN"),
    Chinese_Traditional_BAIDU("cht", "正體中文", "Chinese Traditional","zh-rTW"),
    English_BAIDU("en", "English", "English","en"),
    Japanese_BAIDU("jp","日本語","Japanese","ja"),
    Korean_BAIDU("kor","","Korean","ko"),
    French_BAIDU("fra", "Français", "French","fr"),
    Spanish_BAIDU("spa", "Español", "Spanish","es"),
    Thai_BAIDU("th", "ไทย", "Thai","th"),
    Arabic_BAIDU("ara", "العربية", "Arabic","ar"),
    Russian_BAIDU("ru", "Русский", "Russian","ru"),
    Portuguese_BAIDU("pt", "Português", "Portuguese","pt"),
    German_BAIDU("de", "Deutsch", "German","de"),
    Italian_BAIDU("it", "Italiano", "Italian","it"),
    Greek_BAIDU("el", "Ελληνικά", "Greek","el"),
    Dutch_BAIDU("nl", "Nederlands", "Dutch","nl"),
    Polish_BAIDU("pl", "Polski", "Polish","pl"),
    Bulgarian_BAIDU("bul", "Български", "Bulgarian","bg"),
    Estonian_BAIDU("est", "Eesti", "Estonian","et"),
    Danish_BAIDU("dan", "Dansk", "Danish","da"),
    Finnish_BAIDU("fin", "Suomi", "Finnish","fi"),
    Czech_BAIDU("cs", "Čeština", "Czech","cs"),
    Romanian_BAIDU("rom", "Român", "Romanian","ro"),
    Slovenian_BAIDU("slo", "Slovenščina", "Slovenian","sl"),
    Swedish_BAIDU("swe", "Svenska", "Swedish","sv"),
    Hungarian_BAIDU("hu", "Magyar", "Hungarian","hu"),
    Vietnamese_BAIDU("vie", "Tiếng Việt", "Vietnamese","vi");

    private String languageCode;
    private String languageDisplayName;
    private String languageEnglishDisplayName;
    private String realLanguageCode;

    SupportedLanguages(String languageCode, String languageDisplayName, String languageEnglishDisplayName) {
        this.languageCode = languageCode;
        this.languageDisplayName = languageDisplayName;
        this.languageEnglishDisplayName = languageEnglishDisplayName;
    }
    SupportedLanguages(String languageCode, String languageDisplayName, String languageEnglishDisplayName,String realLanguageCode) {
        this.languageCode = languageCode;
        this.languageDisplayName = languageDisplayName;
        this.languageEnglishDisplayName = languageEnglishDisplayName;
        this.realLanguageCode = realLanguageCode;
    }
    public String getLanguageCode() {
        return languageCode;
    }

    public String getLanguageDisplayName() {
        return languageDisplayName;
    }

    public String getLanguageEnglishDisplayName() {
        return languageEnglishDisplayName;
    }
    public String getRealLanguageCode() {
        return realLanguageCode;
    }

    public static List<SupportedLanguages> getAllSupportedLanguages(TranslationEngineType type) {
        switch (type) {
            case Baidu:
                return getBaiduLanguages();
            case Bing:
                return getBingLanguages();
            case Google:
                return getGoogleLanguages();
        }
        return null;
    }

    public String toString() {
        return getLanguageEnglishDisplayName() + "(\"" + getLanguageCode() + "\", \"" + getLanguageDisplayName() + "\")";
    }

    // get the right value-XX suffix
    public String getAndroidStringFolderNameSuffix() {
        if (this.name().contains("BAIDU")){
            System.out.println(this.toString());
            return this.getRealLanguageCode();
        }
        if (this == Chinese_Simplified_BING || this == Chinese_Simplified)
            return "zh-rCN";
        if (this == Chinese_Traditional_BING || this == Chinese_Traditional)
            return "zh-rTW";
        if (this == Hebrew_BING)
            return Hebrew.getLanguageCode();

        return this.getLanguageCode();
    }
    // google supported language code: https://cloud.google.com/translate/v2/using_rest, language reference section
    private static List<SupportedLanguages> getBaiduLanguages() {
        List<SupportedLanguages> result = new ArrayList<SupportedLanguages>();
        result.add(Chinese_Simplified_BAIDU);
        result.add(Chinese_Traditional_BAIDU);
        result.add(English_BAIDU);
        result.add(Japanese_BAIDU);
        result.add(Korean_BAIDU);
        result.add(French_BAIDU);
        result.add(Spanish_BAIDU);
        result.add(Thai_BAIDU);
        result.add(Arabic_BAIDU);
        result.add(Russian_BAIDU);
        result.add(Portuguese_BAIDU);
        result.add(German_BAIDU);
        result.add(Italian_BAIDU);
        result.add(Greek_BAIDU);
        result.add(Dutch_BAIDU);
        result.add(Polish_BAIDU);
        result.add(Bulgarian_BAIDU);
        result.add(Estonian_BAIDU);
        result.add(Danish_BAIDU);
        result.add(Finnish_BAIDU);
        result.add(Czech_BAIDU);
        result.add(Romanian_BAIDU);
        result.add(Slovenian_BAIDU);
        result.add(Swedish_BAIDU);
        result.add(Hungarian_BAIDU);
        result.add(Vietnamese_BAIDU);
        return result;
    }
    // google supported language code: https://cloud.google.com/translate/v2/using_rest, language reference section
    private static List<SupportedLanguages> getGoogleLanguages() {
        List<SupportedLanguages> result = new ArrayList<SupportedLanguages>();
        result.add(Afrikaans);
        result.add(Albanian);
        result.add(Arabic);
        result.add(Azerbaijani);
        result.add(Basque);
        result.add(Bengali);
        result.add(Belarusian);
        result.add(Bulgarian);
        result.add(Catalan);
        result.add(Chinese_Simplified);
        result.add(Chinese_Traditional);
        result.add(Croatian);
        result.add(Czech);
        result.add(Danish);
        result.add(Dutch);
//        result.add(English);
        result.add(Esperanto);
        result.add(Estonian);
        result.add(Filipino);
        result.add(Finnish);
        result.add(French);
        result.add(Galician);
        result.add(Georgian);
        result.add(German);
        result.add(Greek);
        result.add(Gujarati);
        result.add(Haitian_Creole);
        result.add(Hebrew);
        result.add(Hindi);
        result.add(Hungarian);
        result.add(Icelandic);
        result.add(Indonesian);
        result.add(Irish);
        result.add(Italian);
        result.add(Japanese);
        result.add(Kannada);
        result.add(Korean);
        result.add(Latin);
        result.add(Latvian);
        result.add(Macedonian);
        result.add(Malay);
        result.add(Maltese);
        result.add(Norwegian);
        result.add(Persian);
        result.add(Polish);
        result.add(Portuguese);
        result.add(Romanian);
        result.add(Russian);
        result.add(Serbian);
        result.add(Slovak);
        result.add(Slovenian);
        result.add(Spanish);
        result.add(Swahili);
        result.add(Swedish);
        result.add(Tamil);
        result.add(Telugu);
        result.add(Thai);
        result.add(Turkish);
        result.add(Ukrainian);
        result.add(Urdu);
        result.add(Vietnamese);
        result.add(Welsh);
        result.add(Yiddish);
        return result;
    }

    // bing supported language code: http://msdn.microsoft.com/en-us/library/hh456380.aspx
    private static List<SupportedLanguages> getBingLanguages() {
        List<SupportedLanguages> result = new ArrayList<SupportedLanguages>();
        result.add(Arabic);
        result.add(Bulgarian);
        result.add(Catalan);
        result.add(Chinese_Simplified_BING);
        result.add(Chinese_Traditional_BING);
        result.add(Czech);
        result.add(Danish);
        result.add(Dutch);
        result.add(English);
        result.add(Estonian);
        result.add(Finnish);
        result.add(French);
        result.add(German);
        result.add(Greek);
        result.add(Haitian_Creole);
        result.add(Hebrew_BING);
        result.add(Hindi);
        result.add(Hungarian);
        result.add(Indonesian);
        result.add(Italian);
        result.add(Japanese);
        result.add(Korean);
        result.add(Latvian);
        result.add(Lithuanian);
        result.add(Malay);
        result.add(Maltese);
        result.add(Norwegian);
        result.add(Persian);
        result.add(Polish);
        result.add(Portuguese);
        result.add(Romanian);
        result.add(Russian);
        result.add(Slovak);
        result.add(Slovenian);
        result.add(Spanish);
        result.add(Swedish);
        result.add(Thai);
        result.add(Turkish);
        result.add(Ukrainian);
        result.add(Urdu);
        result.add(Vietnamese);
        result.add(Welsh);
        return result;
    }
}
