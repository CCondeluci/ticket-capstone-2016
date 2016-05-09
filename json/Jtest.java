import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Jtest
{
  public static void main(String[] args) throws IOException
  {
    String[] ticket_categories = {"id", "EffectiveId", "Queue", "Type", "IssueStatement",
        "Resolution", "Owner", "Subject", "InitialPriority", "FinalPriority",
        "Priority", "TimeEstimated", "TimeWorked", "Status", "TimeLeft",
        "Told", "Starts", "Started", "Due", "Resolved", "LastUpdatedBy",
        "LastUpdated", "Creator", "Created", "Disabled"};
    String[] transaction_categories = {"id", "ObjectId", "TimeTaken", "Type",
        "Field", "OldValue", "NewValue", "Data", "Creator", "Created", "ObjectType",
        "ReferenceType", "OldReference", "NewReference"};
    String[] attachment_categories = {"id", "TransactionId", "Parent",
        "MessageId", "Subject", "Filename", "ContentType", "ContentEncoding",
        "Content", "Headers", "Creator", "Created"};

    String[][] ticket_list      = null;
    String[][] transaction_list = null;
    String[][] attachment_list  = null;

    JSON json = new JSON("http://localhost:8008/cakephp/ticket");
    int size = json.SeperateJSONvalues("Tickets", "Ticket");

    ticket_list = json.getAllItems(ticket_categories, size);

    for(int i = 0; i < ticket_list.length; i++)
      System.out.println(Arrays.toString(ticket_list[i]));

    json = new JSON("http://localhost:8008/cakephp/transaction/view/" + ticket_list[0][0]);
    size = json.SeperateJSONvalues("Transactions", "Transaction");
    transaction_list = json.getAllItems(transaction_categories, size);

    for(int i = 0; i < transaction_list.length; i++)
      System.out.println(Arrays.toString(transaction_list[i]));

    json = new JSON("http://localhost:8008/cakephp/attachment/view/" + transaction_list[transaction_list.length-1][0]);
    size = json.SeperateJSONvalues("Attachments", "Attachment");
    attachment_list = json.getAllItems(attachment_categories, size);

    for(int i = 0; i < attachment_list.length; i++)
      System.out.println(Arrays.toString(attachment_list[i]));

    System.out.println();
    System.out.println(attachment_list[0][8]);
  }
}
