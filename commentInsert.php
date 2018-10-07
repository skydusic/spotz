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
    $contents=isset($_POST['contents']) ? $_POST['contents'] : '';

    if($listname == "freeboard"){
        echo "프리보드 들어옴";
        $commentCount = "select * from freeboardCO where postidx='$postidx'";
        $countResult = mysqli_query($connect,$commentCount);
        $count = mysqli_num_rows($countResult);
        
        $sql="insert into freeboardCO(contents,username,postidx,commentidx) 
        values('$contents','$username','$postidx','$count')";
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