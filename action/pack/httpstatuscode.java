package action.pack;

public enum httpstatuscode {
    continue_http(100, "continue"),
    switching_protocol(101, "switching protocols"),
    processing(102, "processing"),

    ok(200, "ok"),
    created(201, "created"),
    accepted(202, "accepted"),
    non_authoritative_information(203, "non-authoritative information"),
    no_content(204,  "no content"),
    reset_content(205, "reset content"),
    partial_content(206, "partial content"),
    multi_status(207, "multi-status (webdav; rfc 4918"),
    already_reported(208, "already reported (webdav; rfc 5842)" ),
    im_used(226, "im used (rfc 3229)"),

    multiple_choices(300, "multiple choices"),
    moved_permanently(301, "moved permanently"),
    found(302, "found"),
    see_other(303, "see other (since http/1.1)"),
    not_modified(304, "not modified"),
    use_proxy(305, "use proxy (since http/1.1)"),
    switch_proxy(306, "switch proxy"),
    temporary_redirect(307, "temporary redirect (since http/1.1)"),
    permanent_redirect(308, "permanent redirect (approved as experimental rfc)[12]"),

    bad_request(400, "bad request"),
    unauthorized(401, "unauthorized"),
    payment_required(402, "payment required"),
    forbidden(403, "forbidden"),
    not_found(404, "not found"),
    method_not_allowed(405, "method not allowed"),
    not_acceptable(406, "not acceptable"),
    proxy_authentication_required(407, "proxy authentication required"),
    request_timeout(408, "request timeout"),
    conflict(409, "conflict"),
    gone(410, "gone"),
    length_required(411, "length required"),
    precondition_failed(412, "precondition failed"),
    request_entity_too_large(413, "request entity too large"),
    request_uri_too_long(414, "request-uri too long"),
    unsupported_media_type(415, "unsupported media type"),
    requested_range_not_satisfiable(416, "requested range not satisfiable"),
    expectation_failed(417, "expectation failed"),

    internal_server_error(500, "internal server error"),
    not_implemented(501, "not implemented"),
    bad_gateway(502, "bad gateway"),
    service_unavailable(503, "service unavailable"),
    gateway_timeout(504, "gateway timeout"),
    http_version_not_supported(505, "http version not supported"),
    variant_also_negotiates(506, "variant also negotiates (rfc 2295)"),
    insufficient_storage(507, "insufficient storage (webdav; rfc 4918)"),
    loop_detected(508, "loop detected (webdav; rfc 5842)"),
    bandwidth_limit_exceeded(509, "bandwidth limit exceeded (apache bw/limited extension)"),
    not_extend(510, "not extended (rfc 2774)"),
    network_authentication_required(511, "network authentication required (rfc 6585)"),
    connection_timed_out(522, "connection timed out"),
    proxy_declined_request(523, "proxy declined request"),
    timeout_occurred(524, "a timeout occurred");

    private Integer code;
    private String desc;
    private String text;

    httpstatuscode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
        this.text = Integer.toString(  code );
    }

    /**
     * gets the http status code
     * @return the status code number
     */
    public Integer getCode() {
        return code;
    }

    /**
     * gets the http status code as a text string
     * @return the status code as a text string
     */
    public String asText() {
        return text;
    }

    /**
     * get the description
     * @return the description of the status code
     */
    public String getDesc() {
        return desc;
    }

}