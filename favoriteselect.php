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

    $username=isset($_POST['username']) ? $_POST['username'] : '';
    $sql = "select * from favorite where username = '$username'";
    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);

    mysqli_data_seek($result, $i);
    $row = mysqli_fetch_array($result);
    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
        
        echo "{\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"image\":\"$row[image]\"}";
    if($i<$total_record-1){
      echo ",";
    }
    }

    echo "]}";

    mysqli_free_result($result);
    mysqli_close($connect);
?>