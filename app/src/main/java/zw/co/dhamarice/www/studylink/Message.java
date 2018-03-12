package zw.co.dhamarice.www.studylink;

/**
 * Created by Ruvimbo on 10/3/2018.
 */

public class Message {

    private String content;

    public Message (){

    }

    public Message (String content ){

        this.content = content;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
