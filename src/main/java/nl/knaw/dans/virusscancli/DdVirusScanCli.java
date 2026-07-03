/*
 * Copyright (C) 2026 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.knaw.dans.virusscancli;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.util.AbstractCommandLineApp;
import nl.knaw.dans.lib.util.ClientProxyBuilder;
import nl.knaw.dans.lib.util.PicocliVersionProvider;
import nl.knaw.dans.virusscancli.client.ApiClient;
import nl.knaw.dans.virusscancli.client.DefaultApi;
import nl.knaw.dans.virusscancli.command.ScanFiles;
import nl.knaw.dans.virusscancli.command.ScanStatus;
import nl.knaw.dans.virusscancli.config.DdVirusScanCliConfig;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "virus-scan",
         mixinStandardHelpOptions = true,
         versionProvider = PicocliVersionProvider.class,
         description = "Command-line client for the dd-virus-scan service")
@Slf4j
public class DdVirusScanCli extends AbstractCommandLineApp<DdVirusScanCliConfig> {
    public static void main(String[] args) throws Exception {
        new DdVirusScanCli().run(args);
    }

    public String getName() {
        return "Command-line client for the dd-virus-scan service";
    }

    @Override
    public void configureCommandLine(CommandLine commandLine, DdVirusScanCliConfig config) {
        log.debug("Configuring command line");
        var api = new ClientProxyBuilder<ApiClient, DefaultApi>()
            .apiClientCtor(ApiClient::new)
            .basePath(config.getVirusScanService().getUrl())
            .httpClient(config.getVirusScanService().getHttpClient())
            .proxyCtor(DefaultApi::new)
            .build();
        var objectMapper = new ObjectMapper();
        commandLine
            .addSubcommand(new ScanFiles(api))
            .addSubcommand(new ScanStatus(api, objectMapper));
    }
}

