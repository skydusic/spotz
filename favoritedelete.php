<?php
    error_reporting(E_ALL);
    ini_set('display_errors',1);

    //$link=mysqli_connect("localhost","root id","root pw","db name");
    $link=mysqli_connect("localhost","spotz","tongood77","spotz");
    if (!$link)
    {
       echo "MySQL 접속 에러 : ";
       echo mysqli_connect_error();
       exit();
    }

    mysqli_set_charset($link,"utf8");
    mysqli_select_db($link,"spotz") or die("Unable to select database");
    session_start();

    $postidx=isset($_POST['postidx']) ? $_POST['postidx'] : '';
    $username=isset($_POST['username']) ? $_POST['username'] : '';
    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';
    
    $sql = "delete from favorite where postidx = '$postidx', username = '$username', listname = '$listname'";
    $result = mysqli_query($link,$sql);

    if($result){
       echo "SQL문 처리 성공";
    }
    else{
       echo "SQL문 처리중 에러 발생 : ";
       echo mysqli_error($link);
    }
    mysqli_close($link);
?>
