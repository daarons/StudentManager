/*
 * Copyright 2016 David Aarons.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.daarons.util;

/**
 *
 * @author David
 */
public class Validator {

    public static boolean isNumber(String text) {
        if (text.matches("^[0-9]+$")) {
            return true;
        }
        return false;
    }
    
    public static boolean containsChinese(String text) {
        return text.codePoints().anyMatch(codepoint
                -> Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }
}
