package action.pack;

public class CrawlerException extends Exception{

    private String code;

    public CrawlerException(String code, String message)
    {
        super(message);
        this.setCode(code);
    }

    public CrawlerException(String code, String message, Throwable cause) {
        super(message, cause);
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void print(){
        System.out.println("Code: "+this.getCode()+" Exception Message : " +this.getMessage());
    }

}
