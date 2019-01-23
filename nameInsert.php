<?php  

    $connect=mysqli_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");
    if (!$connect) {
        $error = mysqli_connect_error();
        $errno = mysqli_connect_errno(); 
        die("Connection failed : "."$errno : $error\n");
        exit(); 
    }
    
    mysqli_set_charset($connect,"utf8");  
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    //POST 값을 읽어온다.
    $username=isset($_POST['username']) ? $_POST['username'] : '';
    $email=isset($_POST['email']) ? $_POST['email'] : '';

    $sql="insert into nameDB(username,email) values('$username','$email')";
    $result=mysqli_query($connect,$sql);
    if($result){
       echo "SQL문 처리 성공";
    }
    else{
       echo "SQL문 처리중 에러 발생 : ";
       echo mysqli_error($connect);
    }
    mysqli_close($connect);
?>