package org.tuvisminds.datatables.data;

public class ActionResult {
    int statusCode;
    String result;

    public ActionResult setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public ActionResult setResult(String result) {
        this.result = result;
        return this;
    }

    public static ActionResult getSuccess() {
        return new ActionResult()
                .setStatusCode(0)
                .setResult("SUCESS");
    }

    public static ActionResult getFailure() {
        return new ActionResult()
                .setStatusCode(1)
                .setResult("FAILED");
    }
}
