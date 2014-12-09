package com.wadpam.guja.api;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.wadpam.guja.readerwriter.DiagnosticsProtoMessageBodyReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.Format;
import java.text.SimpleDateFormat;


/**
 * Allow the box to send diagnostic information to the backend for logging.
 * Logger information can later on be used for troubleshooting and statistics gathering.
 *
 * Current implementation will log the diagnostics to the server log with a specific prefix.
 *
 * @author mattiaslevin
 */
@Path("api/diagnostics")
@Singleton
@PermitAll
public class DiagnosticsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosticsResource.class);

    private static final int SEVERITY_INFO = 0;
    private static final int SEVERITY_WARNING = 1;
    private static final int SEVERITY_ERROR = 2;

    private static final String LOG_STRING_PREFIX = "REMOTE";
    private static final String SEPARATOR = "::";

    private static final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final DiagnosticsLogger logger;


    @Inject
    public DiagnosticsResource(DiagnosticsLogger logger) {
        this.logger = Preconditions.checkNotNull(logger);
    }


    @POST
    @Consumes({MediaType.APPLICATION_JSON, DiagnosticsProtoMessageBodyReader.APPLICATION_X_PROTOBUF})
    @Produces({MediaType.APPLICATION_JSON, DiagnosticsProtoMessageBodyReader.APPLICATION_X_PROTOBUF})
    public Response logDiagnostics(Diagnostics diagnostics) {
        logger.log(diagnostics.getSeverity(), formatLogString(diagnostics));
        return Response.noContent().build();
    }

    // prefix::id=[]::date=[]::tag=[]::info=[]
    private String formatLogString(Diagnostics diagnostics) {

        StringBuilder sb = new StringBuilder();
        sb.append(LOG_STRING_PREFIX).append(SEPARATOR);
        if (null != diagnostics.getId()) {
            sb.append("id=").append(diagnostics.getId()).append(SEPARATOR);
        }
        if (null != diagnostics.getTimestamp()) {
            sb.append("date=").append(formatter.format(new DateTime(diagnostics.getTimestamp()).toDate())).append(SEPARATOR);
        }
        if (null != diagnostics.getTag()) {
            sb.append("tag=").append(diagnostics.getTag()).append(SEPARATOR);
        }
        if (null != diagnostics.getInfo()) {
            sb.append("info=").append(diagnostics.getInfo()).append(SEPARATOR);
        }

        return sb.toString();
    }

    /**
     * Logs diagnostic information.
     */
    public static interface DiagnosticsLogger {
        public void log(int severity, String message);
    }

    /**
     * A default logger that will log to file.
     */
    public static class DefaultDiagnosticLogger implements DiagnosticsLogger {
        @Override
        public void log(int severity, String message) {
            // Build a structured string and write to the sever log
            if (severity == SEVERITY_ERROR) {
                LOGGER.error(message);
            } else if (severity == SEVERITY_WARNING) {
                LOGGER.warn(message);
            } else {
                // Default info
                LOGGER.info(message);
            }
        }
    }

    public static class Diagnostics {

        /**
         * Some kind of id that uniquely identify the reporting app. Mandatory.
         */
        private String id;

        /**
         * Timestamp (in the app) when the event occurred. Mandatory.
         */
        private Long timestamp;

        /**
         * The severity of the diagnostics. Mandatory.
         * 0 = Info
         * 1 = Warning
         * 2 = Error
         */
        private Integer severity;

        /**
         * A string/tag that will be used to categorize the error and simplify search in the future. Optional.
         * The app should decide what tags to use up-front and consistently report diagnostic info towards these tags.
         */
        private String tag;

        /**
         * Free text diagnostic information. Mandatory.
         * The app may employ semantic rules to further simplify search on top of the tag property
         */
        private String info;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public Integer getSeverity() {
            return severity;
        }

        public void setSeverity(Integer severity) {
            this.severity = severity;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
