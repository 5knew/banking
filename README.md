<pre>
 # bankingApp
<hr>
<b>PostMan Requests<br></b>
<p>http://localhost:8080/api/v1/auth/signup</p>
{
  "firstName": "John",<br>
  "lastName": "Doe",<br>
  "login": "user",<br>
  "password": "user",<br>
  "email": "john.e@exaыmsple.co22і45ma",<br>
  "phone": "+12345263ы335s",<br>
  "birthDate": "1990-01-01",<br>
  "initialBalance": 1000.00<br>
}

http://localhost:8080/api/v1/auth/signin<br>
<b>Body:<br></b>
{
"email": "user@gmail.com",<br>
"password": "user"<br>
}

<b>Refresh Token<br></b>
http://localhost:8080/api/v1/auth/refresh
{
"token" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGdtYWlsLmNvbSIsImlhdCI6MTcwOTE0MDM5OCwiZXhwIjoxNzA5NzQ1MTk4fQ.w_sfP3YJ3eYeRE7IB60LBuoLRa0EIPfkwxsvf4M7XM0"<br>
}

<b>Access To User page:</b><br>
http://localhost:8080/api/v1/user
Bearer Token use given Token from Refresh Token so

PatchRequest
http://localhost:8080/api/v1/user/updateEmail
<b>Body</b>
use22@gmail.com

PatchRequest
http://localhost:8080/api/v1/user/deleteContactInfo
<b>Body</b>
{
  "email": "DELETE",
  "phone": ""
}

PatchRequest
http://localhost:8080/api/v1/user/updatePhone
<b>Body</b>
+77475314953

GetRequest
http://localhost:8080/api/v1/user/account/balance
{
    "balance": 1000.0,
    "depositBalance": 0.0
}

PostRequest
http://localhost:8080/api/v1/admin/search?page=0&size=10
{
    "name": "John"
}

PostRequest
http://localhost:8080/api/v1/user/transferToDeposit
{
    "amount": 300.00
}

PostRequest
http://localhost:8080/api/v1/user/transfer
{
    "toUserLogin": "user",
    "amount": 500.00
}
</pre>


