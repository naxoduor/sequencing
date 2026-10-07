package com.bio.sequencing.plugins.external_tool_support.hmmer;

import com.bio.sequencing.plugins.external_tool_support.mafft.AbstractTask;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ParseHMMERResultTask
        extends AbstractTask {

    private final HMMERSettings settings;

    private final HMMERExecutionResult executionResult;

    private HMMERResultDocument resultDocument;

    public ParseHMMERResultTask(
            HMMERSettings settings,
            HMMERExecutionResult executionResult) {

        super("Parse HMMER result");

        this.settings = settings;

        this.executionResult =
                executionResult;
    }

    @Override
    protected void execute()
            throws Exception {

        resultDocument =
                new HMMERResultDocument();

        resultDocument.setQueryFile(
                settings.getSequenceFile());

        resultDocument.setHmmFile(
                settings.getHmmFile());

        resultDocument.setDatabase(
                settings.getHmmDatabase());

        resultDocument.setRawOutputFile(
                executionResult.getRawOutputFile());

        resultDocument.setTableOutputFile(
                executionResult.getTableOutputFile());

        resultDocument.setDomainTableOutputFile(
                executionResult.getDomainTableOutputFile());

        if (executionResult.getTableOutputFile()
                != null) {

            parseTable(
                    executionResult.getTableOutputFile(),
                    resultDocument);
        }

        if (executionResult.getDomainTableOutputFile()
                != null) {

            parseDomainTable(
                    executionResult
                            .getDomainTableOutputFile(),
                    resultDocument);
        }
    }

    private void parseTable(
            String filename,
            HMMERResultDocument document)
            throws IOException {

        Path path =
                Paths.get(filename);

        if (!Files.exists(path)) {
            return;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(path)) {

            String line;

            while ((line =
                    reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()
                        || line.startsWith("#")) {

                    continue;
                }

                String[] fields =
                        line.split("\\s+");

                /*
                 * HMMER --tblout:
                 *
                 * 0 target name
                 * 1 accession
                 * 2 target length
                 * 3 query name
                 * 4 accession
                 * 5 query length
                 * 6 E-value
                 * 7 score
                 * 8 bias
                 * 9 exp
                 * 10 reg
                 * 11 clu
                 * 12 ov
                 * 13 env
                 * 14 dom
                 * 15 rep
                 * 16 inc
                 * 17 description
                 */

                if (fields.length < 18) {
                    continue;
                }

                HMMERHit hit =
                        new HMMERHit();

                hit.setTargetName(
                        fields[0]);

                hit.setTargetAccession(
                        normalize(fields[1]));

                hit.setTargetLength(
                        parseInt(fields[2]));

                hit.setQueryName(
                        fields[3]);

                hit.setQueryAccession(
                        normalize(fields[4]));

                hit.setQueryLength(
                        parseInt(fields[5]));

                hit.setSequenceEvalue(
                        parseDouble(fields[6]));

                hit.setSequenceScore(
                        parseDouble(fields[7]));

                hit.setSequenceBias(
                        parseDouble(fields[8]));

                StringBuilder description =
                        new StringBuilder();

                for (int i = 17;
                     i < fields.length;
                     i++) {

                    if (i > 17) {
                        description.append(" ");
                    }

                    description.append(fields[i]);
                }

                hit.setDescription(
                        description.toString());

                document.getHits().add(hit);
            }
        }
    }

    private void parseDomainTable(
            String filename,
            HMMERResultDocument document)
            throws IOException {

        Path path =
                Paths.get(filename);

        if (!Files.exists(path)) {
            return;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(path)) {

            String line;

            while ((line =
                    reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()
                        || line.startsWith("#")) {

                    continue;
                }

                String[] fields =
                        line.split("\\s+");

                /*
                 * HMMER --domtblout:
                 *
                 * 0  target name
                 * 1  accession
                 * 2  target length
                 * 3  query name
                 * 4  accession
                 * 5  query length
                 * 6  full-sequence E-value
                 * 7  full-sequence score
                 * 8  full-sequence bias
                 * 9  domain number
                 * 10 total domains
                 * 11 c-Evalue
                 * 12 i-Evalue
                 * 13 domain score
                 * 14 domain bias
                 * 15 hmm from
                 * 16 hmm to
                 * 17 ali from
                 * 18 ali to
                 * 19 env from
                 * 20 env to
                 * 21 accuracy
                 * 22 description
                 */

                if (fields.length < 23) {
                    continue;
                }

                HMMERDomain domain =
                        new HMMERDomain();

                domain.setTargetName(
                        fields[0]);

                domain.setTargetAccession(
                        normalize(fields[1]));

                domain.setQueryName(
                        fields[3]);

                domain.setQueryAccession(
                        normalize(fields[4]));

                domain.setDomainNumber(
                        parseInt(fields[9]));

                domain.setTotalDomains(
                        parseInt(fields[10]));

                domain.setcEvalue(
                        parseDouble(fields[11]));

                domain.setiEvalue(
                        parseDouble(fields[12]));

                domain.setScore(
                        parseDouble(fields[13]));

                domain.setBias(
                        parseDouble(fields[14]));

                domain.setHmmFrom(
                        parseInt(fields[15]));

                domain.setHmmTo(
                        parseInt(fields[16]));

                domain.setAliFrom(
                        parseInt(fields[17]));

                domain.setAliTo(
                        parseInt(fields[18]));

                domain.setEnvFrom(
                        parseInt(fields[19]));

                domain.setEnvTo(
                        parseInt(fields[20]));

                domain.setAccuracy(
                        parseDouble(fields[21]));

                StringBuilder description =
                        new StringBuilder();

                for (int i = 22;
                     i < fields.length;
                     i++) {

                    if (i > 22) {
                        description.append(" ");
                    }

                    description.append(fields[i]);
                }

                domain.setDescription(
                        description.toString());

                document.getDomains().add(domain);
            }
        }
    }

    private String normalize(
            String value) {

        if ("-".equals(value)) {
            return null;
        }

        return value;
    }

    private int parseInt(String value) {

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDouble(String value) {

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public HMMERResultDocument getResultDocument() {
        return resultDocument;
    }
}