/**
 * Copyright 2016-2018 Bryan Pikaard & Nicholas Sylke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.typicalbot;

import com.typicalbot.core.shard.ShardManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TypicalBot {
    public TypicalBot() throws IOException, InterruptedException {
        if (!Files.exists(Paths.get(System.getProperty("user.dir")).resolve("config"))) {
            Files.createDirectory(Paths.get(System.getProperty("user.dir")).resolve("config"));
        }

        if (!Files.exists(Paths.get(System.getProperty("user.dir")).resolve("config/app.yml"))) {
            export(Paths.get(System.getProperty("user.dir")).resolve("config/app.yml"), "/config/app.yml");
        }

        ShardManager.register(1);
    }

    public void export(Path dest, String resource) {
        InputStream stream = TypicalBot.class.getResourceAsStream(resource);
        try {
            Files.copy(stream, dest);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        try {
            new TypicalBot();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}