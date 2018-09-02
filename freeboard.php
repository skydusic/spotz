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

    $idx=isset($_POST['idx']) ? $_POST['idx'] : '';
    $listname=isset($_POST['listname']) ? $_POST['listname'] : '';
    $spindata=isset($_POST['spindata']) ? $_POST['spindata'] : '';

    if($listname == "freeboard"){
        if($spindata == "전체"){
            $sql ="select * from freeboard order by created desc";
        } else {
            $sql = "select * from freeboard where spindata = '$spindata' order by created desc";
        } 
        
    } else if($listname == "freelancer"){
        if($spindata1 == "전국"){
            $sql ="select * from freelancer order by created desc, addedtime desc";
        } else if($spindata2 != ""){
            $sql = "select * from freelancer where spindata2 = '$spindata2' order by created desc, addedtime desc";
        } else if ($spindata1 != "" || $spindata2 == ""){
            $sql = "select * from freelancer where spindata1 = '$spindata1' order by created desc, addedtime desc";
        }
    } else if($listname == "competition"){
        if($spindata1 == "전국"){
            $sql ="select * from competition order by created desc, addedtime desc";
        } else if($spindata2 != ""){
            $sql = "select * from competition where spindata2 = '$spindata2' order by created desc, addedtime desc";
        } else if ($spindata1 != "" || $spindata2 == ""){
            $sql = "select * from competition where spindata1 = '$spindata1' order by created desc, addedtime desc";
        }
    } else if($listname == "dongho"){
        if($spindata1 == "전국"){
            $sql ="select * from dongho order by created desc, addedtime desc";
        } else if($spindata2 != ""){
            $sql = "select * from dongho where spindata2 = '$spindata2' order by created desc, addedtime desc";
        } else if ($spindata1 != "" || $spindata2 == ""){
            $sql = "select * from dongho where spindata1 = '$spindata1' order by created desc, addedtime desc";
        }
    } else if($listname == "review"){
        if($spindata1 == "전국"){
            $sql ="select * from review order by created desc, addedtime desc";
        } else if($spindata2 != ""){
            $sql = "select * from review where spindata2 = '$spindata2' order by created desc, addedtime desc";
        } else if ($spindata1 != "" || $spindata2 == ""){
            $sql = "select * from review where spindata1 = '$spindata1' order by created desc, addedtime desc";
        }
    } else if($listname == "employment"){
        if($spindata1 == "전국"){
            $sql ="select * from employment order by created desc, addedtime desc";
        } else if($spindata2 != ""){
            $sql = "select * from employment where spindata2 = '$spindata2' order by created desc, addedtime desc";
        } else if ($spindata1 != "" || $spindata2 == ""){
            $sql = "select * from employment where spindata1 = '$spindata1' order by created desc, addedtime desc";
        }
    }
    
    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);

    echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
    {
        mysqli_data_seek($result, $i);
        $row = mysqli_fetch_array($result);
        
        if($listname == "freeboard"){
            echo "{\"listname\":\"freeboard\",\"idx\":$row[idx],\"title\":\"$row[title]\",\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata\":\"$row[spindata]\"}";
        } else if ($listname == "freelancer"){
            $sql2 = "select * from freeextension where postidx = '$row[idx]'";
            $result2 = mysqli_query($connect,$sql2);
            mysqli_data_seek($result2, 0);
            $row2 = mysqli_fetch_array($result2);
            echo "{\"listname\":\"freelancer\",\"idx\":$row[idx],\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata1\":\"$row[spindata1]\",\"spindata2\":\"$row[spindata2]\",\"text1\":\"$row2[name]\",\"text2\":\"$row2[sports]\",\"text3\":\"$row2[location]\",\"text4\":\"$row2[phone]\",\"text5\":\"$row2[etc]\"}";
        } else if ($listname == "competition"){
            $sql2 = "select * from competitionextension where postidx = '$row[idx]'";
            $result2 = mysqli_query($connect,$sql2);
            mysqli_data_seek($result2, 0);
            $row2 = mysqli_fetch_array($result2);
            echo "{\"listname\":\"competition\",\"idx\":$row[idx],\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata1\":\"$row[spindata1]\",\"spindata2\":\"$row[spindata2]\",\"text1\":\"$row2[name]\",\"text2\":\"$row2[sports]\",\"text3\":\"$row2[location]\",\"text4\":\"$row2[phone]\",\"text5\":\"$row2[etc]\"}";
        } else if ($listname == "dongho"){
            $sql2 = "select * from donghoextension where postidx = '$row[idx]'";
            $result2 = mysqli_query($connect,$sql2);
            mysqli_data_seek($result2, 0);
            $row2 = mysqli_fetch_array($result2);
            echo "{\"listname\":\"dongho\",\"idx\":$row[idx],\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata1\":\"$row[spindata1]\",\"spindata2\":\"$row[spindata2]\",\"text1\":\"$row2[name]\",\"text2\":\"$row2[sports]\",\"text3\":\"$row2[location]\",\"text4\":\"$row2[time]\",\"text5\":\"$row2[phone]\"}";
        } else if ($listname == "review"){
            $sql2 = "select * from reviewextension where postidx = '$row[idx]'";
            $result2 = mysqli_query($connect,$sql2);
            mysqli_data_seek($result2, 0);
            $row2 = mysqli_fetch_array($result2);
            echo "{\"listname\":\"review\",\"idx\":$row[idx],\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata1\":\"$row[spindata1]\",\"spindata2\":\"$row[spindata2]\",\"text1\":\"$row2[product]\",\"text2\":\"$row2[wheretobuy]\",\"text3\":\"$row2[price]\",\"text4\":\"$row2[greade]\",\"text5\":\"$row2[etc]\"}";
        } else if ($listname == "employment"){
            $sql2 = "select * from employmentextension where postidx = '$row[idx]'";
            $result2 = mysqli_query($connect,$sql2);
            mysqli_data_seek($result2, 0);
            $row2 = mysqli_fetch_array($result2);
            echo "{\"listname\":\"employment\",\"idx\":$row[idx],\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"hit\":\"$row[hit]\",\"image\":\"$row[image]\",\"spindata1\":\"$row[spindata1]\",\"spindata2\":\"$row[spindata2]\",\"text1\":\"$row2[company]\",\"text2\":\"$row2[location]\",\"text3\":\"$row2[salary]\",\"text4\":\"$row2[calendar]\",\"text5\":\"$row2[etc]\"}";
        }
        
        if($i<$total_record-1){
          echo ",";
        }
    }

    echo "]}";

    mysqli_free_result($result);
    mysqli_close($connect);
?>