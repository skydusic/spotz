<?php
    $connect=mysqli_connect( "localhost", "spotz", "tongood77");
    if (!$connect) {
        $error = mysqli_connect_error();
        $errno = mysqli_connect_errno(); 
        die("Connection failed : "."$errno : $error\n");
        exit(); 
    }

    mysqli_set_charset($connect,"utf8");
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    $postidx=isset($_POST['postidx']) ? $_POST['postidx'] : '';
    $username=isset($_POST['username']) ? $_POST['username'] : '';
    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';
    
    $sql = "delete from favorite where postidx = '$postidx'AND username = '$username'AND listname = '$listname'";
    $result = mysqli_query($connect,$sql);

    if($connect){
       echo "SQL문 처리 성공";
    }
    else{
       echo "SQL문 처리중 에러 발생 : ";
       echo mysqli_error($connect);
    }
    mysqli_free_result($result);
    mysqli_close($connect);
?>
