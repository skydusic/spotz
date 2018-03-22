<?php

    $connect=mysqli_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");

    mysqli_set_charset($connect,"utf8");  
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';
    $idx=isset($_POST['idx']) ? $_POST['idx'] : '';

    if($listname == 'clubtable'){
        $sql = "select * from clubtable where idx='$idx'";
    } else if ($listname == 'freelancer'){
        $sql = "select * from freelancer where idx='$idx'";
    }


    $result = mysqli_query($connect,$sql);
    $pre = mysqli_fetch_array($result);
    
    $hit = $pre[hit] + 1;
    
// strtotime() 함수 사용 -> 시간 조정
    $time = date("Y-m-d H:i:s",strtotime('+5minutes'));

    if($listname == 'clubtable'){
        $sql = "update clubtable set hit = '$hit' where idx='$idx'";
    } else if ($listname == 'freelancer'){
        $sql = "update freelancer set hit = '$hit' where idx='$idx'";
    }
    $result2 = mysqli_query($connect,$sql);

    if($result2){
       echo "SQL문 처리 성공";
    }
    else{
       echo "SQL문 처리중 에러 발생 : ";
       echo mysqli_error($connect);
    }

    if($result !=null){
        mysqli_free_result($result);
    }
?>