package javax.finance.stockquotes.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import javax.finance.stockquotes.constant.StockQuotesApplicationConstants;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ApiErrorDto implements Serializable {

    private static final long serialVersionUID = 5836829424427117679L;

    private HttpStatus httpStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ",
            timezone = StockQuotesApplicationConstants.DEFAULT_TIMEZONE_ID)
    private Date timestamp;

    private String message;
    private String debugMessage;
    private List<ApiErrorDto> rootErrors;

    public ApiErrorDto(final HttpStatus httpStatus, final Date timestamp, final String message,
                       final String debugMessage, final List<ApiErrorDto> rootErrors) {
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
        this.message = message;
        this.debugMessage = debugMessage;
        this.rootErrors = rootErrors;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(final HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(final String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public List<ApiErrorDto> getRootErrors() {
        return rootErrors;
    }

    public void setRootErrors(final List<ApiErrorDto> rootErrors) {
        this.rootErrors = rootErrors;
    }

}
