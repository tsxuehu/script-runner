package org.tsxuehu.scriptrunner;

import org.apache.commons.cli.CommandLine;

import java.util.Map;

public class Utils {
    public static void parseEnv(CommandLine cliParser, String optionKey, Map<String, String> shellEnv) {
        if (cliParser.hasOption(optionKey)) {
            String envs[] = cliParser.getOptionValues(optionKey);
            for (String env : envs) {
                env = env.trim();
                int index = env.indexOf('=');
                if (index == -1) {
                    shellEnv.put(env, "");
                    continue;
                }
                String key = env.substring(0, index);
                String val = "";
                if (index < (env.length() - 1)) {
                    val = env.substring(index + 1);
                }
                shellEnv.put(key, val);
            }
        }
    }
}
