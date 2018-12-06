<?php
    $connect=mysqli_connect( "localhost", "spotz", "tongood77");
    if (!$connect) {
        $error = mysqli_connect_error();
        $errno = mysqli_connect_errno(); 
        die("Connection failed : "."$errno : $error\n");
        exit(); 
    }

    mysqli_set_charset($connect,"utf-8");  
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    $postidx=isset($_POST['postidx']) ? $_POST['postidx'] : '';
    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';
    $username=isset($_POST['username']) ? $_POST['username'] : '';
    $commentidx=isset($_POST['commentidx']) ? $_POST['commentidx'] : '';

    if($listname == "freeboard"){
        $sql="delete from freeboardCO WHERE postidx = '$postidx' AND commentidx = '$commentidx'";
    } else if($listname == "kbl"){
        $sql="delete from kblCO WHERE postidx = '$postidx' AND commentidx = '$commentidx'";
    } else if($listname == "nba"){
        $sql="delete from nbaCO WHERE postidx = '$postidx' AND commentidx = '$commentidx'";
    } else if($listname == "equip"){
        $sql="delete from equipCO WHERE postidx = '$postidx' AND commentidx = '$commentidx'";
    } else if($listname == "employ"){
        $sql="delete from employCO WHERE postidx = '$postidx' AND commentidx = '$commentidx'";
    } else if($listname == "compet"){
        $sql="delete from competCO WHERE postidx = '$postidx' AND commentidx = '$commentidx'";
    }


    $result=mysqli_query($connect,$sql);  
    if($result){
       echo "SQL문 처리 성공";
    }
    else{
       echo "SQL문 처리중 에러 발생 : ";
       echo mysqli_error($connect);
    }

    mysqli_free_result($result);
    mysqli_close($connect);
?>