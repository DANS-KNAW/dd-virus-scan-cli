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
package nl.knaw.dans.virusscancli.command;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.virusscancli.api.StartFileScanRequestDto;
import nl.knaw.dans.virusscancli.client.DefaultApi;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "scan-files",
         mixinStandardHelpOptions = true,
         description = "Start an on-demand virus scan for a list of Dataverse file IDs.")
@RequiredArgsConstructor
@Slf4j
public class ScanFiles implements Callable<Integer> {

    @NonNull
    private final DefaultApi api;

    @Option(names = { "--file-id" },
            required = true,
            description = "Dataverse file ID to scan. Repeat to scan multiple files.",
            arity = "1..*")
    private List<Long> fileIds;

    @Override
    public Integer call() {
        try {
            var request = new StartFileScanRequestDto();
            request.setFileIds(fileIds);
            var response = api.startFileScan(request);
            System.out.println(response.getJobId());
            return 0;
        }
        catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            log.error("Error starting file scan", e);
            return 1;
        }
    }
}
