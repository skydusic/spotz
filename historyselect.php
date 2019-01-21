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
    $email=isset($_POST['email']) ? $_POST['email'] : '';

    $sql = "select * from history where email = '$email' ORDER BY created DESC";
    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);
    

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
        mysqli_data_seek($result, $i);
        $row = mysqli_fetch_array($result);
        
        if($row[listname] == "freeboard"){
            $sql1 = "select * from freeboard where idx = $row[postidx]";
            echo "{\"listname\":\"freeboard\",";
        } else if($row[listname] == "kbl"){
            $sql1 = "select * from kbl where idx = $row[postidx]";
            echo "{\"listname\":\"kbl\",";
        } else if($row[listname] == "equip"){
            $sql1 = "select * from equip where idx = $row[postidx]";
            echo "{\"listname\":\"equip\",";
        } else if($row[listname] == "qna"){
            $sql1 = "select * from qna where idx = $row[postidx]";
            echo "{\"listname\":\"qna\",";
        } else if($row[listname] == "compet"){
            $sql1 = "select * from compet where idx = $row[postidx]";
            echo "{\"listname\":\"compet\",";
        } else if($row[listname] == "nba"){
            $sql1 = "select * from nba where idx = $row[postidx]";
            echo "{\"listname\":\"nba\",";
        }
        
        $re = mysqli_query($connect,$sql1);
        $row1 = mysqli_fetch_array($re);

        echo "\"idx\":$row1[idx],\"title\":\"$row1[title]\",\"contents\":\"$row1[contents]\",\"username\":\"$row1[username]\",\"email\":\"$row1[email]\",\"created\":\"$row1[created]\",\"hit\":\"$row1[hit]\",\"image\":\"$row1[image]\",\"spindata\":\"$row1[spindata]\"}";
        
        
        if($i<$total_record-1){
            echo ",";
        }
    }

    echo "]}";

    mysqli_free_result($result);
    mysqli_close($connect);
?>