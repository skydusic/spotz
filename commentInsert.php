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
    $email=isset($_POST['email']) ? $_POST['email'] : '';
    $contents=isset($_POST['contents']) ? $_POST['contents'] : '';

    if($listname == "freeboard"){
        $commentCount = "SELECT * FROM freeboardCO WHERE postidx='$postidx' ORDER BY commentidx DESC";
        $countResult = mysqli_query($connect,$commentCount);
        mysqli_data_seek($countResult, 0);
        $row = mysqli_fetch_array($countResult);
        $count = $row[commentidx] + 1;
        $sql="insert into freeboardCO(contents,username,email,postidx,commentidx) 
        values('$contents','$username','$email','$postidx','$count')";
    } else if($listname == "kbl"){
        $commentCount = "SELECT * FROM kblCO WHERE postidx='$postidx' ORDER BY commentidx DESC";
        $countResult = mysqli_query($connect,$commentCount);
        mysqli_data_seek($countResult, 0);
        $row = mysqli_fetch_array($countResult);
        $count = $row[commentidx] + 1;
        $sql="insert into kblCO(contents,username,email,postidx,commentidx) 
        values('$contents','$username','$email','$postidx','$count')";
    } else if($listname == "nba"){
        $commentCount = "SELECT * FROM nbaCO WHERE postidx='$postidx' ORDER BY commentidx DESC";
        $countResult = mysqli_query($connect,$commentCount);
        mysqli_data_seek($countResult, 0);
        $row = mysqli_fetch_array($countResult);
        $count = $row[commentidx] + 1;
        $sql="insert into nbaCO(contents,username,email,postidx,commentidx) 
        values('$contents','$username','$email','$postidx','$count')";
    } else if($listname == "equip"){
        $commentCount = "SELECT * FROM equipCO WHERE postidx='$postidx' ORDER BY commentidx DESC";
        $countResult = mysqli_query($connect,$commentCount);
        mysqli_data_seek($countResult, 0);
        $row = mysqli_fetch_array($countResult);
        $count = $row[commentidx] + 1;
        $sql="insert into equipCO(contents,username,email,postidx,commentidx) 
        values('$contents','$username','$email','$postidx','$count')";
    } else if($listname == "qna"){
        $commentCount = "SELECT * FROM qnaCO WHERE postidx='$postidx' ORDER BY commentidx DESC";
        $countResult = mysqli_query($connect,$commentCount);
        mysqli_data_seek($countResult, 0);
        $row = mysqli_fetch_array($countResult);
        $count = $row[commentidx] + 1;
        $sql="insert into qnaCO(contents,username,email,postidx,commentidx) 
        values('$contents','$username','$email','$postidx','$count')";
    } else if($listname == "compet"){
        $commentCount = "SELECT * FROM competCO WHERE postidx='$postidx' ORDER BY commentidx DESC";
        $countResult = mysqli_query($connect,$commentCount);
        mysqli_data_seek($countResult, 0);
        $row = mysqli_fetch_array($countResult);
        $count = $row[commentidx] + 1;
        $sql="insert into competCO(contents,username,email,postidx,commentidx) 
        values('$contents','$username','$email','$postidx','$count')";
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