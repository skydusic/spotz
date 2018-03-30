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

    $sql = "select * from favorite where username = '$username' order by created";
    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);
    

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
        mysqli_data_seek($result, $i);
        $row = mysqli_fetch_array($result);
        
        if($row[listname] == "clubtable"){
            $sql1 = "select * from clubtable where idx = $row[postidx]";
            echo "{\"listname\":\"clubtable\",";
        } else if($row[listname] == "freelancer"){
            $sql1 = "select * from freelancer where idx = $row[postidx]";
            echo "{\"listname\":\"freelancer\",";
        } else if($row[listname] == "competition"){
            $sql1 = "select * from competition where idx = $row[postidx]";
            echo "{\"listname\":\"competition\",";
        } else if($row[listname] == "dongho"){
            $sql1 = "select * from dongho where idx = $row[postidx]";
            echo "{\"listname\":\"dongho\",";
        } else if($row[listname] == "review"){
            $sql1 = "select * from review where idx = $row[postidx]";
            echo "{\"listname\":\"review\",";
        } else if($row[listname] == "employment"){
            $sql1 = "select * from employment where idx = $row[postidx]";
            echo "{\"listname\":\"employment\",";
        }
        $result1 = mysqli_query($connect,$sql1);
        $row1 = mysqli_fetch_array($result1);
        echo "\"idx\":$row1[idx],\"title\":\"$row1[title]\",\"contents\":\"$row1[contents]\",\"username\":\"$row1[username]\",\"created\":\"$row1[created]\",\"image\":\"$row1[image]\"}";
        
    if($i<$total_record-1){
      echo ",";
    }
    }

    echo "]}";

    mysqli_free_result($result);
    mysqli_close($connect);
?>