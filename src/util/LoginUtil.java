package util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class LoginUtil {
    public AddCartUtil addCartUtil = new AddCartUtil();

    public static void main(String[] args){
        LoginUtil lu=new LoginUtil();
        String res=lu.login("111@163.com","121");
        System.out.println(res);
    }

    public String login(String email,String password){
        String res="";

        try{
            JSONObject json;
            json = JSONObject.parseObject(addCartUtil.read_json());
            JSONArray jsonUser = json.getJSONArray("user");

            for(int i=0;i<jsonUser.size();i++){
                JSONObject item = (JSONObject) jsonUser.get(i);
                if(item.getString("email").equals(email)){
                    if(item.getString("password").equals(password))
                    {
                        res+=item.getString("name");
                        return res;
                    }
                    else{
                        res+="Error password!";
                        return res;
                    }
                }
            }
            res+="Unregistered user!";
            return res;
        }catch (JSONException ex) {
            ex.printStackTrace();
        }

        return res;
    }

    public String register(String name,String email,String password){
        String res="";
        JSONArray user_arry=new JSONArray();
        JSONObject res_json=new JSONObject();

        try{
            JSONObject json;
            json = JSONObject.parseObject(addCartUtil.read_json());
            JSONArray jsonUser = json.getJSONArray("user");

            //数据库中存在邮箱
            for(int i=0;i<jsonUser.size();i++){
                JSONObject item = (JSONObject) jsonUser.get(i);
                if(item.getString("email").equals(email)){
                    res+="The email has been registered";
                    return res;
                }
                else{
                    user_arry.add(item);
                }
            }
            //添加用户
            res+="Successfully Registered!";
            JSONObject user_item=new JSONObject();
            user_item.put("name",name);
            user_item.put("email",email);
            user_item.put("password",password);
            user_arry.add(user_item);

            res_json.put("user",user_arry);
            res_json.put("cart",json.getJSONArray("cart"));
            res_json.put("order",json.getJSONArray("order"));
        }catch (JSONException ex) {
            ex.printStackTrace();
        }

        addCartUtil.write_json(res_json);
        return res;
    }
}
