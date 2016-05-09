import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class JSON
{

  URL url;
  HttpURLConnection request;
  JsonElement root;
  JsonObject rootobj;
  JsonArray ja;
  String[][] list;
  ArrayList<JsonObject> objectlist = new ArrayList();

  public JSON(String site) throws IOException
  {
    url = new URL(site);
    request = (HttpURLConnection) url.openConnection();
    request.connect();
  }

  /*
  String item example: "Tickets"
  String item2 example: "Ticket"
  Try to follow this convention for now
  Helps deal with cakephp results when it comes to single and multiple json outputs
   */
  public int SeperateJSONvalues(String item, String item2) throws IOException
  {
    JsonParser jp = new JsonParser();

    root = jp.parse((new InputStreamReader((InputStream) request.getContent())));
    rootobj = root.getAsJsonObject();
    System.out.println(rootobj.toString());
    if(rootobj.has(item))
    {
      root = rootobj.get(item);
    }
    else
    {
      root = rootobj.get(item2);
    }

    if(root.isJsonArray())
    {
      ja = root.getAsJsonArray();
      System.out.println(ja.size());
      System.out.println(ja.toString());
      for(int i = 0; i < ja.size(); i++)
      {
        objectlist.add(ja.get(i).getAsJsonObject().get(item2).getAsJsonObject());
        System.out.println(objectlist.get(i));
      }
      return ja.size();

    }
    else
    {
      System.out.println(root.toString());
      rootobj = root.getAsJsonObject();
      root = rootobj.get(item2);
      objectlist.add(root.getAsJsonObject());
      System.out.println(rootobj.toString());
      return 1;
    }
  }

  public String[][] getAllItems(String[] categories, int size)
  {
    list = new String[size][];


    for(int i = 0; i < list.length; i++)
    {
      rootobj = objectlist.get(i);
      String[] items = new String[categories.length];
      for(int j = 0; j < categories.length; j++)
      {
        if(rootobj.get(categories[j]).isJsonNull())
        {
          items[j] = null;
        }
        else
        {
          items[j] = rootobj.get(categories[j]).getAsString();
        }
      }
      list[i] = items;
    }
    return list;
  }

    /*public void getJSONitems(int number, String header) {
        for (int i = 0; i < number; i++) {
            list.add(ja.get(i).getAsJsonObject().get(header).getAsJsonObject());
            System.out.println(list.get(i));
        }
    }

    public String getJSONelement(int number, String element) {
        if (list.get(number).get(element).isJsonNull()) {
            return null;
        }
        return list.get(number).get(element).getAsString();

    }

    public String[] getAllElements(int number, String[] elements) {
        String[] items = new String[elements.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = getJSONelement(number, elements[i]);
            //System.out.println(items[i]);
        }
        return items;
    } */
}
