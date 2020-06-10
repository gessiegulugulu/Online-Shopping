<h3 style="text-align:center;text-decoration:none">web个人项目报告</h3>

---
**目录**

[TOC]
#### 一、 项目结构
---

##### 1. 前端

根据项目要求，考虑实现6个功能：商品展示，用户注册，用户登录，添加购物车，结算，查看历史订单。我将6个项目分为以下四个页面实现，放在web文件夹中。其下的js文件夹包含所有js文件，css文件夹包含css文件，resource文件夹包含界面用到的图片、icon等。

* login.html

  处理**登录**和**注册**事件，与login.js绑定，实现用户错误操作提示和AJAX请求。

* index.html

  进行**商品的展示**，包括商品图片、名称、星级、历史价格和当前价格、加入购物车按钮。以tab的形式对商品进行简单的分类。并允许用户跳转到登录和购物车界面。与ui.js绑定，处理tab动效和**加入购物车**的动作。

* cart.html

  进行购物车的展示，包括商品的图片、名称、价格、数量、总价、删除按钮，允许对购物车内商品进行数量增加和删除的操作。与cart.js绑定，处理**结算**事件，将购物车内的物品加入历史订单中。
  
* orders.html

  进行历史**订单的展示**，包括订单序号、订单事件、订单详情、订单总金额。与orders.js绑定。

html的样式在文件ui.css中进行规定，没有考虑窗口大小的适配。

##### 2. 后端

​		后端用JSP实现，servlet处理前端的AJAX请求，放在src文件夹中。servlet文件夹中的文件，分别相应购物车界面、登录、注册、商品展示界面传回的请求，并调用util文件夹中的功能函数，将函数返回的结果回传到前端界面。util文件夹中的文件，实现对数据库的读写操作。下图为项目整体代码结构。

<img src="项目报告/image-20200527160301215.png" alt="image-20200527160301215" style="zoom: 55%;" />

#### 二、 功能实现
---
##### 1. 注册

（1）用例事件流

* 基本事件流
  * 用户输入昵称、邮箱、密码
  * 用户重新输入一次密码
  * 点击注册，系统弹出注册成功提示并刷新login页面
* 备选流
  * 用户输入的邮箱已被注册：系统弹出邮箱已被注册提示，并刷新页面
  * 用户两次输入的密码不一致：系统弹出两次输入的密码不一致提示，并刷新页面

（2）前端样式

​		注册整体使用form的格式，便于获取数据。form外套div，编写为带边框卡片样式用户输入框的div统一class并编写样式。核心html和css代码如下：

```html
<div class="login-register-col">
	<div class="login-reg-form-wrap sign-up-form">
    	<h4>注册</h4>
        <form action="login.html" method="post" id="register-form">
        	<div class="single-input-item">
            	<input type="text" placeholder="昵称" required/>
            </div>
            <div class="single-input-item">
            	<input type="email" placeholder="邮箱" required/>
            </div>
            <div class="single-input-item">
            	<input type="password" placeholder="输入密码" required/>
            </div>
            <div class="single-input-item">
            	<input type="password" placeholder="重新输入密码" required/>
            </div>
            <div class="single-input-item">
            	<button onclick="registerSubmit()" class="btn-sqr">注册</button>
            </div>
        </form>
    </div>
</div>
```

```css
.single-input-item {
    margin-top: 20px;
}

.single-input-item input{
    color: #555555;
    border: 1px solid #ccc;
    padding: 12px 10px;
    width: 100%;
    font-size: 14px;
    background: #f7f7f7;
}

.single-input-item input:active, .single-input-item input:focus{
    border-color: #ec7259;
    background-color: #fff;
}

.single-input-item .btn-sqr {
    color: #fff;
    font-size: 14px;
    border-radius: 0;
    background-color: #ec7259;
    padding: 13px 25px;
    line-height: 0.7;
}

.single-input-item .btn-sqr:hover {
    color: #fff;
    background-color: #222222;
}
```

（3）用户交互

​		用户交互主要处理点击注册之后与后端的交互动作，使用了AJAX请求。并弹窗显示从后端返回的注册信息，分为邮箱已注册、成功注册两种。主要代码如下：

```js
function registerSubmit(){
    var form=document.getElementById("register-form");
    var input=form.getElementsByTagName('input');
    var name=input[0].value;
    var email=input[1].value;
    var password=input[2].value;
    var check_password=input[3].value;
    if(password==check_password){
        var url="register?name="+name+"&email="+email+"&password="+password;

        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200 || xhr.status == 304) {
                var data=xhr.responseText;
                alert(data);
            }
        };
        xhr.send();
    }
    else{
        alert("The entered passwords do not match");
    }
}
```



##### 2. 登录

（1）用例事件流

* 基本事件流
  * 用户输入邮箱密码
  * 用户点击登录
  * 系统跳转到主页

* 备选流
  * 用户输入密码与该邮箱对应密码不匹配：系统提示密码输入错误，刷新界面
  * 用户输入邮箱在数据库不存在 ：系统提示邮箱不存在 ，刷新界面 

（2）前端样式

​		前端样式和注册功能相似，都是同样class的卡片样式包含form，form内的input也是统一设置的。

（3）用户交互

​		用户交互主要处理点击登录之后与后端的交互动作，使用了AJAX请求。从后端返回的登录提示或用户名，返回用户名，则用户成功登录，系统将用户名暂存在localstorage中。其他情况则登录出现问题，系统出现弹窗提示。主要代码如下：

```js
function formSubmit() {
    var form=document.getElementById("login-form");
    var input=form.getElementsByTagName('input');
    var email=input[0].value;
    var password=input[1].value;
    var url="login?email="+email+"&password="+password;

    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200 || xhr.status == 304) {
            var data=xhr.responseText;
            console.log(data);
            if(data=="Error password!"||data=="Unregistered user!"){
                alert(data);
            }
            else{
                localStorage.setItem("user_email", data);
                window.location.href="index.html";
            }
        }
    };
    xhr.send();
}
```



##### 3. 商品展示

​		商品展示界面主要内容是将商品分为三个tab，当用户鼠标悬停在tab选项上时，显示相应商品。商品放于product-item的class中，整个的布局用表格来控制。tab页的实现用js来控制，当鼠标悬停在tab选项上时，将对应类添加active名称，其他的active则去掉。

​		tab-content为商品三个tab的总容器，tab-pane为单个tab的容器。product-tab-menu为tab选项的容器。

```html
<div class="product-tab-menu">
    <ul id="product-tab-menu-list">
        <li class="active">服饰</li>
        <li>家装</li>
        <li>食品</li>
    </ul>
</div>
<div id="tab-content">
	<div class="tab-pane active" id="tab1"> ...
	</div>
	<div class="tab-pane" id="tab2"> ...
	</div>
	<div class="tab-pane" id=" tab3"> ..
	</div>
</div>
```
​		当tab-pan添加了active类名之后， 变为可见状态。而不具有active的tab-pane都为不可见状态。

```css
#tab-content .tab-pane {
    display: block;
    height: 0;
    max-width: 100%;
    opacity: 0;
    overflow: hidden;
    visibility: hidden;
}

#tab-content .tab-pane.active {
    height: auto;
    opacity: 1;
    overflow: visible;
    visibility: visible;
}
```

​		展示动效的js核心代码如下：

```js
// 商品分类展示tab动效
var tab_menu = document.getElementById('product-tab-menu-list').getElementsByTagName('li');
var tab_content=[];
tab_content[0] = document.getElementById('tab1');
tab_content[1] = document.getElementById('tab2');
tab_content[2] = document.getElementById('tab3');
for (var i = 0; i < 3; i++) {
    tab_menu[i].id = i;
    tab_menu[i].onmouseover = function () {
        for (var j = 0; j < 3; j++) {
            tab_menu[j].classList.remove('active');
            tab_content[j].classList.remove('active');
        }
        this.classList.add('active');
        tab_content[this.id].classList.add('active');
    }
}
```



##### 4. 添加购物车&购物车查看

（1）用例事件流

* 用户点击商品卡片内的购物车按钮
* 系统将该商品加入数据库
* 系统跳转到购物车界面

（2）前端样式

​		购物车界面显示商品的图片、名称、单价、数量、总价，以及整个购物车的结算价格。购物车商品的数量，仿照淘宝可以进行加减，同时总价和购物车结算价格也随之变化。也可以对购物车的某个商品进行删除操作，该行商品信息将全部删除，购物车结算价格也进行相应的变化。

​		购物车类似商品展示界面，也是使用表格布局，对图片列、名称列、单价列、数量列、总价列、删除按钮列，进行统一的class命名和样式处理。下面是表格中一行商品信息的核心代码：

```html
<td class="pro-thumbnail"><img class="img-fluid" src="resource/product/wearing-1.jpg" alt=""/>
</td>
<td class="pro-title">Fashion Overall</td>
<td class="pro-price"><span>$87.00</span></td>
<td class="pro-quantity">
    <div class="pro-qty">
        <span class="dec qtybtn">-</span>
        <input id="temp" type="text" value="2">
        <span class="inc qtybtn">+</span>
    </div>
</td>
<td class="pro-subtotal"><span>$174.00</span></td>
<td class="pro-remove"><img class="pro-remove-icon" src="resource/icon/delete.png" alt=""></td>
```

```css
.cart-table-container .cart-table tr .pro-thumbnail,
.cart-table-container .cart-table tr .pro-price,
.cart-table-container .cart-table tr .pro-subtotal,
.cart-table-container .cart-table tr .pro-title,
.cart-table-container .cart-table tr .pro-remove {
    width: 20%;
}

.cart-table-container .cart-table tr .pro-quantity {
    width: 100px;
}

.cart-table-container .cart-table tr .pro-qty {
    width: 90px;
    height: 40px;
    border: 1px solid #ddd;
    padding: 0 15px;
    float: left;
}

.cart-table-container .cart-table tr .pro-qty .qtybtn {
    width: 15px;
    display: block;
    float: left;
    line-height: 38px;
    cursor: pointer;
    text-align: center;
    font-size: 22px;
    font-weight: 400;
    color: #555555;
}

.cart-table-container .cart-table tr .pro-qty input {
    width: 28px;
    float: left;
    border: none;
    height: 40px;
    line-height: 34px;
    padding: 0;
    text-align: center;
    background-color: transparent;
    outline: none;
}
```

（3）用户交互

​		加入购物车的交互，是当用户点击按钮后，将对应商品信息发送到后端。后端将在购物车数据中查找对应商品是否已经存在在购物车中，若存在，则数量加一；若不存在，则将该商品加入购物车数据。并将购物车数据返回前端，当系统跳转到购物车界面时，将返回的购物车数据展示出来。其中AJAX请求用到了封装好的post函数，写在了post.js文件中。主要js代码如下：

```js
function add_to_cart(item) {
    if(localStorage.getItem("user_email")==""){
        alert("请登录账户！");
    }
    else {
        var title = item.parentNode.getElementsByClassName('product-description')[0].innerText;
        console.log("hi from add_to_cart", item, title);
        var price_con = item.parentNode.getElementsByClassName('product-price')[0];
        var price = price_con.getElementsByClassName('price-new')[0].innerText;
        var img = item.parentNode.parentNode.childNodes[1];
        var thumbnail = img.src;
        var url = "test?thumbnail=" + thumbnail + "&title=" + title + "&price=" + price + "&quantity=1&subtotal=" + price;
        // alert(url);
        post(url);
        window.location.href = "cart.html";
    }

}
```

​		购物车主要的用户交互为商品数量的加减，和商品的删除。都是通过获取html上的点击事件，找到点击所在的商品行，对需要改变的元素进行处理。



##### 5. 结算&订单查看

（1）用例事件流

* 用户点击购物车界面的结算按钮
* 系统弹出确认总金额的对话框
* 用户点击确认 
* 系统跳转到订单界面 

（2）前端样式

​		订单界面的样式和购物车类似，也是以table的格式逐行打印对应的信息，并统一对列样式进行设置。主要html代码如下，orders-pane为订单整体的容器，orders-content为整体订单的容器，orders-content-table为订单表格的容器。

```html
<div class="orders-content">
    <h5>历史订单</h5>
    <div class="orders-content-table">
        <table class="orders-content-product-table">
            <thead class="orders-table-thead"> .
            </thead>
            <tbody> ..
            </tbody>
        </table>
    </div>
</div>
```

（3）用户交互

​		交互操作与加入购物车类似，用户点击结算后，系统将向后端发送请求，后端将当前购物车数据处理成一条订单信息，加入订单数据中，并返回给前端。在系统跳转到订单界面的时候将数据展示出来。

#### 三、 界面效果

* 注册登录界面

  ![image-20200527201800768](项目报告/image-20200527201800768.png)

* 商品展示界面

  ![image-20200527201828024](项目报告/image-20200527201828024.png)

* 购物车界面

  ![image-20200527201958260](项目报告/image-20200527201958260.png)

* 订单界面

  ![image-20200527202016193](项目报告/image-20200527202016193.png)

#### 四、 待改进部分

* 购物车的数量加减和删除，有时存在不响应的情况。思考原因可能是，加载数据到html界面的方式存在问题，导致元素的响应失效，考虑可以改变加载的方法。
* 目前的结算采取结算整个购物车的方法，但是在实际生活中，是采取在购物车中选择商品进行结算的。应该在每个商品行都加上复选框，在结算时只提取复选框选中的数据。
* 登录和注册界面，当用户的输入存在问题时，为了简便，我直接采取了刷新界面等待用户重新输入的方法。但实际上应该部分更新input，保留用户没有问题的输入。