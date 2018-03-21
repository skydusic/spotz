<?php

    $connect=mysql_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");

    mysql_query("SET NAMES UTF8");

    mysql_select_db("spotz",$connect) or die("Unable to select database");

    session_start();


    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';
    $idx=isset($_POST['idx']) ? $_POST['idx'] : '';

    if($listname == 'clubtable'){
        $sql = "select * from clubtable where idx='$idx'";
    }


    $result = mysql_query($sql, $connect);
    $pre = mysql_fetch_array($result);
    
    $up = $pre[up] + 1;
    
// strtotime() 함수 사용 -> 시간 조정
    $time = date("Y-m-d H:i:s",strtotime('+5minutes'));

    if($listname == 'clubtable'){
        $sql = "update clubtable set up = '$up', addedtime = '$time' where idx='$idx'";
    }
    $result2 = mysql_query($sql, $connect);

    if($result2){
       echo "SQL문 처리 성공";
    }
    else{
       echo "SQL문 처리중 에러 발생 : ";
       echo mysqli_error($connect);
    }
    mysql_close();
?>