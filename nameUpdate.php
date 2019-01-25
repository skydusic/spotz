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
    $username=isset($_POST['username']) ? $_POST['username'] : '';
    $email=isset($_POST['email']) ? $_POST['email'] : '';

    $sql = "select * from nameDB where email = '$email'";
    
    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);

    $sql1 = "select * from nameDB where username = '$username'";
    $result1 = mysqli_query($connect,$sql1);
    $total_record1 = mysqli_num_rows($result1);

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"result\":";
    
    //동일 닉네임 확인
    if($total_record1 == 0) {
        //DB내 동일 이메일 확인
        if($total_record == 0) {
            $sql="insert into nameDB(username,email) values('$username','$email')";
            $result1 = mysqli_query($connect,$sql);
        } else if($total_record == 1) {
            $sql ="UPDATE nameDB SET username = '$username' where email = '$email'";
            $result1 = mysqli_query($connect,$sql);
        }
        echo "\"성공\"";

    } else if($total_record1 == 1){
        echo "\"실패\"";
    }

    echo "}";

    if($result1){
       echo "SQL문 처리 성공";
    }
    else{
       echo "SQL문 처리중 에러 발생 : ";
       echo mysqli_error($connect);
    }
    mysqli_close($connect);
?>