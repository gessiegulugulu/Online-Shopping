package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.parser.JSONParser;


import java.io.*;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AddCartUtil {


    public static void main(String[] args) {
        AddCartUtil tu = new AddCartUtil();
        tu.add_cart("resource/product/wearing-1.jpg","工装裤", "￥87.00", "1", "￥87.00");
//        System.out.println("res:" + r);
    }

    //返回添加购物车后的json string写入文件
    public boolean add_cart(String thumbnail,String title, String price, String quantity, String subtotal) {
        JSONArray res_cart_json = new JSONArray();//处理后的cart json串
		JSONObject res_json=new JSONObject();
        try {
            JSONObject json;
            json = JSONObject.parseObject(read_json());
            JSONArray jsonCart = json.getJSONArray("cart");

            boolean flag = true;
            for (int i = 0; i < jsonCart.size(); i++) {
                JSONObject item = (JSONObject) jsonCart.get(i);
                String cart_title = item.getString("title");
                if (cart_title.equals(title)) {
                    int cart_quantity = item.getInteger("quantity") + 1;
                    float float_total = Float.parseFloat(price.substring(1)) * cart_quantity;
                    DecimalFormat decimalFormat = new DecimalFormat(".00");
                    String cart_subtotal = "$" + decimalFormat.format(float_total);

                    JSONObject cart_item = new JSONObject();
                    cart_item.put("thumbnail", thumbnail);
                    cart_item.put("quantity", cart_quantity);
                    cart_item.put("price", price);
                    cart_item.put("subtotal", cart_subtotal);
                    cart_item.put("title", title);

                    res_cart_json.add(cart_item);
                    flag = false;
                } else {
                    res_cart_json.add(item);
                }
            }
            if (flag) {
                JSONObject cart_item = new JSONObject();
                cart_item.put("quantity", quantity);
                cart_item.put("price", price);
                cart_item.put("subtotal", subtotal);
                cart_item.put("title", title);
                cart_item.put("thumbnail", thumbnail);
                res_cart_json.add(cart_item);
            }

			res_json.put("user",json.getJSONArray("user"));
			res_json.put("cart",res_cart_json);
			res_json.put("order",json.getJSONArray("order"));
            System.out.println("res_json:"+res_json);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        write_json(res_json);
        read_json();

        return true;
    }

    public void write_json(JSONObject json){
		BufferedWriter bw = null;
		String path = "";
		String jsonStr = "";

		String classpath = getClass().getResource("").getPath();
		String[] list = classpath.split("/");
		for (int i = 0; i < list.length - 1; i++) {
			path += list[i] + "/";
		}
		path += "data.json";
		try {
			path = URLDecoder.decode(path, "UTF-8");
//			"D:\\@Codes\\WebProject\\homework\\src\\data.json"
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"utf-8"));
//			bw.write(json);
            json.writeJSONString(bw);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    //返回json文件的string格式
    public String read_json() {
        BufferedReader br = null;
        String path = "";
        String jsonStr = "";

        String classpath = getClass().getResource("").getPath();
        String[] list = classpath.split("/");
        for (int i = 0; i < list.length - 1; i++) {
            path += list[i] + "/";
        }
        path += "data.json";
        try {
            path = URLDecoder.decode(path, "UTF-8");
//			"D:\\@Codes\\WebProject\\homework\\src\\data.json"
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                jsonStr += line.trim();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    //不管执行是否出现异常，必须确保关闭BufferedReader
                    br.close();
                } catch (IOException e1) {
                }
            }
        }
        System.out.println("read_file:"+jsonStr);
        return jsonStr;
    }

    public String read_cart_json(){
        JSONObject json;
        json = JSONObject.parseObject(read_json());
        JSONArray jsonCart = json.getJSONArray("cart");
        return jsonCart.toString();
    }

    public void order(String order_id,String order_time,String order_detail,String order_subtotal){
        JSONArray res_cart_json = new JSONArray();//处理后的cart json串
        JSONObject res_json=new JSONObject();
        try {
            JSONObject json;
            json = JSONObject.parseObject(read_json());
            JSONObject order_item = new JSONObject();
            order_item.put("order_id", order_id);
            order_item.put("order_time", order_time);
            order_item.put("order_detail", order_detail);
            order_item.put("order_subtotal", order_subtotal);

            JSONArray jsonCart = json.getJSONArray("order");

            for (int i = 0; i < jsonCart.size(); i++) {
                JSONObject item = (JSONObject) jsonCart.get(i);
                res_cart_json.add(item);
            }
            res_cart_json.add(order_item);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return;
    }
}
