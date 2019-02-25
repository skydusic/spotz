<?php

    $connect=mysqli_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");

    mysqli_set_charset($connect,"utf8");  
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    $username=isset($_POST['username']) ? $_POST['username'] : '';
    $email=isset($_POST['email']) ? $_POST['email'] : '';

    $sql = "select * from postlist where email = '$email' ORDER BY created DESC";
    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

    
    for ($i=0; $i < $total_record; $i++) {
        
        mysqli_data_seek($result, $i);
        $row = mysqli_fetch_array($result);
        
        if($row[listname] == "freeboard"){
            $sql = "select * from freeboard where idx = $row[postidx]";
            echo "{\"listname\":\"freeboard\",";
        } else if($row[listname] == "kbl"){
            $sql = "select * from kbl where idx = $row[postidx]";
            echo "{\"listname\":\"kbl\",";
        } else if($row[listname] == "nba"){
            $sql = "select * from nba where idx = $row[postidx]";
            echo "{\"listname\":\"nba\",";
        } else if($row[listname] == "equip"){
            $sql = "select * from equip where idx = $row[postidx]";
            echo "{\"listname\":\"equip\",";
        } else if($row[listname] == "qna"){
            $sql = "select * from qna where idx = $row[postidx]";
            echo "{\"listname\":\"qna\",";
        } else if($row[listname] == "compet"){
            $sql = "select * from compet where idx = $row[postidx]";
            echo "{\"listname\":\"compet\",";
        }
        
        $result1 = mysqli_query($connect,$sql);
        $row1 = mysqli_fetch_array($result1);
        
        echo "\"idx\":$row1[idx],\"title\":\"$row1[title]\",\"username\":\"$row1[username]\",\"email\":\"$row1[email]\",\"created\":\"$row1[created]\",\"hit\":\"$row1[hit]\",\"image\":\"$row1[image]\",\"spindata\":\"$row1[spindata]\"}";
        
        if($i<$total_record-1){
          echo ",";
        }
    }

    echo "]}";
//    mysqli_free_result($connect);
    mysqli_close($connect);
?>