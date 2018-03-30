<?php
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

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

    $username=isset($_POST['username']) ? $_POST['username'] : '';
    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';
    $postidx=isset($_POST['postidx']) ? $_POST['postidx'] : '';

//    $sql = "select * from history where username = '$username' order by idx";
    $sql = "select * from history where username = '$username' and listname = '$listname' order by idx";

    $result = mysqli_query($link,$sql);
    $total_record = mysqli_num_rows($result);

    $rimit = 20;
    if($total_record >= $rimit){
        mysqli_data_seek($result, $rimit);
        $row = mysqli_fetch_array($result);
        $sql = "delete from history where idx = '$row[idx]'";
        $result2 = mysqli_query($link,$sql);
        
        if($result){
           echo "SQL문 delete 처리 성공";
        }
        else{
           echo "SQL문 처리중 에러 발생 : ";
           echo mysqli_error($link);
        }
    }

    $sql = "select * from history where username = '$username' and postidx = '$postidx' order by idx desc";
    $result = mysqli_query($link,$sql);
    $row = mysqli_fetch_array($result);
    $posttime = strtotime($row[created]);
    $time = date('Y-m-d H:i:s',time());
    $time = strtotime($time);
    $temp = $time - $posttime;

    if($temp < 1800){
        $sql = "delete from history where idx = $row[idx]";
        $result = mysqli_query($link,$sql);
        if($result){
           echo "60초 이하 del 처리 성공";
        }
        else{
           echo "60초 이하 del 처리중 에러 발생 : ";
           echo mysqli_error($link);
        }
    }

    $sql ="insert into history (username,postidx,listname) values('$username','$postidx','$listname')";
    $result3 = mysqli_query($link,$sql);
    if($result3){
       echo "SQL문 insert 처리 성공";
    }
    else{
       echo "SQL문 insert 처리중 에러 발생 : ";
       echo mysqli_error($link);
    }
    mysqli_close($link);
?>