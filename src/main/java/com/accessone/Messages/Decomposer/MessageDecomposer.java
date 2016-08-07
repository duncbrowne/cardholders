package com.accessone.Messages.Decomposer;

import com.accessone.Database.MariaDBFacade;

import javax.json.*;
import java.io.StringReader;

/**
 * Current Project cardholders
 * Created by duncan.browne on 07/08/2016.
 */
public class MessageDecomposer
{
    private static MessageDecomposer ourInstance = new MessageDecomposer();

    public static MessageDecomposer getInstance()
    {
        return ourInstance;
    }

    private MessageDecomposer()
    {
    }

    public String decomposeMessage(String strMessage)
    {
        JsonReader jsonReader = Json.createReader(new StringReader(strMessage));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();

        JsonObject dataObject = object.getJsonObject("data");
        JsonObject getRequestObject = dataObject.getJsonObject("get");

        if (!getRequestObject.isEmpty())
        {
            String strGetType = getRequestObject.getString("type");
            System.out.println("Get type : " + strGetType);
            if (strGetType.equals("cardholder"))
            {
                String strID = getRequestObject.getString("value");
                System.out.println("Cardholder ID : " + strID);

                //Get the record from the DB
                JsonObject joCardholderRecord = MariaDBFacade.getInstance().GetCardholderRecord(Integer.parseInt(strID));

                JsonObject requesterObject = object.getJsonObject("requester");
                JsonBuilderFactory factory = Json.createBuilderFactory(null);
                if (!joCardholderRecord.isEmpty())
                {
                    JsonObject joReturnMsg = factory.createObjectBuilder()
                            .add("requester", requesterObject)
                            .add("status", "success")
                            .add("data", factory.createObjectBuilder()
                                .add("post", joCardholderRecord))
                            .build();

                    System.out.println("Return message : " + joReturnMsg.toString());
                    return joReturnMsg.toString();
                }

            }
        }
        else
        {
            System.out.println("get request message was empty");
        }

        return null;
    }

    private String createReplyMessage(String strReceivedMessage, String status, String strResponse)
    {
        return null;
    }
}
