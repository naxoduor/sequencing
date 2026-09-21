package com.bio.sequencing.workers;

import com.bio.sequencing.models.AnalysisRequest;
import com.bio.sequencing.models.AnalysisResult;
import com.bio.sequencing.port.Port;
import com.bio.sequencing.workflow.socket.WorkflowEventPublisher;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;

import java.util.Map;

public class BioconductorWorker implements Worker{

    private Port input;

    private Port output;

    private WorkflowEventPublisher eventPublisher;

    private String workflowId;

    private String nodeId;


    private RConnection rConnection;

//    public BioconductorWorker(String workflowId){
//        this.workflowId = workflowId;
//    }

    public BioconductorWorker(Port input, Port output){
        this.input = input;
        this.output = output;
    }

    public BioconductorWorker(String workflowId,
                              String nodeId,
                              Port input,
                              Port output,
                              WorkflowEventPublisher eventPublisher, WorkflowEventPublisher eventPublisher1, String workflowId1, String nodeId1) {

        this.input = input;
        this.output = output;
        this.eventPublisher = eventPublisher1;
        this.workflowId = workflowId1;
        this.nodeId = nodeId1;
    }

    public BioconductorWorker(String id, Object nodeId, Object input) {
    }

    /**
     * Initialize the worker and establish the connection
     * to the R/Bioconductor runtime.
     */
    public void init() {

        try {

            rConnection = new RConnection();

            // Test R connection
            rConnection.eval("R.version.string");

            System.out.println(
                    "Connected to R/Bioconductor"
            );

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Unable to initialize Bioconductor worker",
                    e
            );
        }
    }

    /**
     * Process one analysis request.
     */
    public void tick() {

//        AnalysisRequest request = input.receive();
        String request = input.get().toString();

        if (request == null) {
            return;
        }

        AnalysisResult result;

        try {

            result = executeBioconductor(request);
            eventPublisher.portData(
                    workflowId,
                    nodeId,
                    "result",
                    result
            );

            eventPublisher.workerCompleted(
                    workflowId,
                    nodeId
            );


        } catch (Exception e) {

            result = AnalysisResult.failed(
                    request,
                    e.getMessage()
            );

            eventPublisher.workerFailed(
                    workflowId,
                    nodeId,
                    e.getMessage()
            );

        }

        output.put(result);
    }

    @Override
    public boolean isDone() {
        return false;
    }

    /**
     * Execute a Bioconductor operation using R.
     */
    private AnalysisResult executeBioconductor(
            String request) {

//        String jobId = request.getJobId();
        String jobId = request;



        AnalysisResult result =
                AnalysisResult.running(jobId);

        try {

            if (rConnection == null) {
                throw new IllegalStateException(
                        "Bioconductor worker has not been initialized"
                );
            }

            /*
             * 1. Load Bioconductor package
             */
//            String packageName =
//                    request.getPackageName();
            String packageName = request;

            String loadPackage =
                    "library(" +
                            quoteR(packageName) +
                            ")";

            rConnection.eval(loadPackage);

            /*
             * 2. Build the R operation.
             */
            String expression =
                    buildExpression(new AnalysisRequest(request, null, null));

            /*
             * 3. Execute the operation.
             */
            REXP rResult =
                    rConnection.parseAndEval(expression);

            /*
             * 4. Store result.
             *
             * For a real system you would normally serialize
             * the result to a file rather than converting every
             * R object to a String.
             */
            String resultString =
                    rResult.toString();

            result.getResults().put(
                    "value",
                    resultString
            );

            result.getResults().put(
                    "operation",
                    request
            );

            result.getResults().put(
                    "package",
                    request
            );

            result.setStatus(
                    AnalysisResult.Status.COMPLETED
            );

            result.setMessage(
                    "Bioconductor analysis completed"
            );

            result.setCompletedAt(
                    java.time.Instant.now()
            );

            return result;

        } catch (Exception e) {

            return AnalysisResult.failed(
                    jobId,
                    e.getMessage()
            );
        }
    }

    /**
     * Converts AnalysisRequest into an R expression.
     */
    private String buildExpression(
            AnalysisRequest request) {

        String operation =
                request.getOperation();

        Map<String, String> inputs =
                request.getInput();

        Map<String, Object> parameters =
                request.getParameters();

        StringBuilder expression =
                new StringBuilder();

        /*
         * Example:
         *
         * findOverlaps(
         *     query,
         *     subject,
         *     ignore.strand=TRUE
         * )
         */

        expression
                .append(operation)
                .append("(");

        boolean first = true;

        /*
         * Add input files.
         */
        for (Map.Entry<String, String> entry :
                inputs.entrySet()) {

            if (!first) {
                expression.append(",");
            }

            expression
                    .append(entry.getKey())
                    .append("=")
                    .append(
                            quoteR(entry.getValue())
                    );

            first = false;
        }

        /*
         * Add parameters.
         */
        for (Map.Entry<String, Object> entry :
                parameters.entrySet()) {

            if (!first) {
                expression.append(",");
            }

            expression
                    .append(entry.getKey())
                    .append("=")
                    .append(
                            convertToRValue(
                                    entry.getValue()
                            )
                    );

            first = false;
        }

        expression.append(")");

        return expression.toString();
    }

    /**
     * Convert Java values into R values.
     */
    private String convertToRValue(Object value) {

        if (value == null) {
            return "NULL";
        }

        if (value instanceof Boolean b) {
            return b ? "TRUE" : "FALSE";
        }

        if (value instanceof Number) {
            return value.toString();
        }

        if (value instanceof String s) {
            return quoteR(s);
        }

        throw new IllegalArgumentException(
                "Unsupported R parameter type: "
                        + value.getClass()
        );
    }

    /**
     * Safely quote a Java String for R.
     */
    private String quoteR(String value) {

        String escaped =
                value
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"");

        return "\"" + escaped + "\"";
    }

    /**
     * Shut down the R connection.
     */
    public void shutdown() {

        if (rConnection != null) {

            try {
                rConnection.close();
            } catch (Exception ignored) {
            }

            rConnection = null;
        }
    }
}