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

package com.example.wujs.data;


import com.example.wujs.module.FilterRule;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wesley Lin on 12/17/14.
 */
public class SerializeUtil {

    public static String serializeFilterRuleList(List<FilterRule> rules) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rules.size(); i++) {
            if (i != 0)
                sb.append("\n");

            sb.append(rules.get(i).getFilterRuleType().toName())
                    .append("<>")
                    .append(rules.get(i).getFilterString());
        }
        return sb.toString();
    }

    public static List<FilterRule> deserializeFilterRuleList(String ruleString) {
        List<FilterRule> rules = new ArrayList<FilterRule>();
        String[] tokens = ruleString.split("\n");
        for (int i = 0; i < tokens.length; i++) {
            String[] values = tokens[i].split("<>");
            if (values.length == 2) {
                rules.add(new FilterRule(FilterRule.FilterRuleType.fromName(values[0]), values[1]));
            }
        }
        return rules;
    }
}
