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

    $username=isset($_POST['username']) ? $_POST['username'] : '';
    $email=isset($_POST['email']) ? $_POST['email'] : '';

    $sql = "select * from nameDB where email = '$email'";
    
    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";
    
    mysqli_data_seek($result, 0);
    $row = mysqli_fetch_array($result);
    echo "{\"listname\":\"nameDB\",\"email\":\"$row[email]\",\"username\":\"$row[username]\"}";

    echo "]}";

    mysqli_free_result($result);
    mysqli_close($connect);
?>