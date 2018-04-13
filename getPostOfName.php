<?php

    $connect=mysqli_connect( "localhost", "spotz", "tongood77") or die( "Unable to connect to SQL server");

    mysqli_set_charset($connect,"utf8");  
    mysqli_select_db($connect,"spotz") or die("Unable to select database");
    session_start();

    $username=isset($_POST['username']) ? $_POST['username'] : '';

    $sql = "select * from freelancer where username = '$username' ORDER BY created DESC";
    $result = mysqli_query($connect,$sql);
    $total_record = mysqli_num_rows($result);

    $sql2 = "select * from clubtable where username = '$username' ORDER BY created DESC";
    $result2 = mysqli_query($connect,$sql2);
    $total_record2 = mysqli_num_rows($result2);

    $sql3 = "select * from competition where username = '$username' ORDER BY created DESC";
    $result3 = mysqli_query($connect,$sql3);
    $total_record3 = mysqli_num_rows($result3);
    
    $sql4 = "select * from dongho where username = '$username' ORDER BY created DESC";
    $result4 = mysqli_query($connect,$sql4);
    $total_record4 = mysqli_num_rows($result4);

    $sql5 = "select * from review where username = '$username' ORDER BY created DESC";
    $result5 = mysqli_query($connect,$sql5);
    $total_record5 = mysqli_num_rows($result5);

    $sql6 = "select * from employment where username = '$username' ORDER BY created DESC";
    $result6 = mysqli_query($connect,$sql6);
    $total_record6 = mysqli_num_rows($result6);

    $total = $total_record + $total_record2 + $total_record3 + $total_record4 + $total_record5 + $total_record6;

    echo "{\"status\":\"OK\",\"num_results\":\"$total\",\"results\":[";

    for ($i=0; $i < $total_record; $i++)
        {
            mysqli_data_seek($result, $i);
            $row = mysqli_fetch_array($result);
            $sqlEx = "select * from freeextension where postidx = '$row[idx]'";
            $resultex = mysqli_query($connect,$sqlEx);
            mysqli_data_seek($resultex, 0);
            $rowex = mysqli_fetch_array($resultex);
            echo "{\"listname\":\"freelancer\",\"idx\":$row[idx],\"contents\":\"$row[contents]\",\"username\":\"$row[username]\",\"created\":\"$row[created]\",\"image\":\"$row[image]\",\"hit\":\"$row[hit]\",\"spindata1\":\"$row[spindata1]\",\"spindata2\":\"$row[spindata2]\",\"postidx\":\"$rowex[postidx]\",\"text1\":\"$rowex[name]\",\"text2\":\"$rowex[sports]\",\"text3\":\"$rowex[location]\",\"text4\":\"$rowex[phone]\",\"text5\":\"$rowex[etc]\"}";

            if($i<$total_record){
                echo ",";
            }
        }

    for ($i=0; $i < $total_record2; $i++)
    {
        mysqli_data_seek($result2, $i);
        $row2 = mysqli_fetch_array($result2);
        $sql2Ex = "select * from clubextension where postidx = '$row2[idx]'";
        $result2ex = mysqli_query($connect,$sql2Ex);
        mysqli_data_seek($result2ex, 0);
        $row2ex = mysqli_fetch_array($result2ex);
        echo "{\"listname\":\"clubtable\",\"idx\":\"$row2[idx]\",\"postidx\":\"$row2[postidx]\",\"contents\":\"$row2[contents]\",\"username\":\"$row2[username]\",\"created\":\"$row2[created]\",\"image\":\"$row2[image]\",\"hit\":\"$row2[hit]\",\"spindata1\":\"$row2[spindata1]\",\"spindata2\":\"$row2[spindata2]\",\"postidx\":\"$row2ex[postidx]\",\"text1\":\"$row2ex[corperation]\",\"text2\":\"$row2ex[sports]\",\"text3\":\"$row2ex[location]\",\"text4\":\"$row2ex[phone]\",\"text5\":\"$row2ex[etc]\"}";

        if($i<$total_record2){
            echo ",";
        }
    }

    for ($i=0; $i < $total_record3; $i++)
    {
        mysqli_data_seek($result3, $i);
        $row3 = mysqli_fetch_array($result3);
        $sql3Ex = "select * from competitionextension where postidx = '$row3[idx]'";
        $result3ex = mysqli_query($connect,$sql3Ex);
        mysqli_data_seek($result3ex, 0);
        $row3ex = mysqli_fetch_array($result3ex);
        echo "{\"listname\":\"clubtable\",\"idx\":\"$row3[idx]\",\"postidx\":\"$row3[postidx]\",\"contents\":\"$row3[contents]\",\"username\":\"$row3[username]\",\"created\":\"$row3[created]\",\"image\":\"$row3[image]\",\"hit\":\"$row3[hit]\",\"spindata1\":\"$row3[spindata1]\",\"spindata2\":\"$row3[spindata2]\",\"postidx\":\"$row3ex[postidx]\",\"text1\":\"$row3ex[name]\",\"text2\":\"$row3ex[sports]\",\"text3\":\"$row3ex[location]\",\"text4\":\"$row3ex[phone]\",\"text5\":\"$row3ex[etc]\"}";

        if($i<$total_record3){
            echo ",";
        }
    }

    for ($i=0; $i < $total_record4; $i++)
    {
        mysqli_data_seek($result4, $i);
        $row4 = mysqli_fetch_array($result4);
        $sql4Ex = "select * from donghoextension where postidx = '$row4[idx]'";
        $result4ex = mysqli_query($connect,$sql4Ex);
        mysqli_data_seek($result4ex, 0);
        $row4ex = mysqli_fetch_array($result4ex);
        echo "{\"listname\":\"clubtable\",\"idx\":\"$row4[idx]\",\"postidx\":\"$row4[postidx]\",\"contents\":\"$row4[contents]\",\"username\":\"$row4[username]\",\"created\":\"$row4[created]\",\"image\":\"$row4[image]\",\"hit\":\"$row4[hit]\",\"spindata1\":\"$row4[spindata1]\",\"spindata2\":\"$row4[spindata2]\",\"postidx\":\"$row4ex[postidx]\",\"text1\":\"$row4ex[name]\",\"text2\":\"$row4ex[sports]\",\"text3\":\"$row4ex[location]\",\"text4\":\"$row4ex[time]\",\"text5\":\"$row4ex[phone]\"}";

        if($i<$total_record4){
            echo ",";
        }
    }

    for ($i=0; $i < $total_record5; $i++)
    {
        mysqli_data_seek($result5, $i);
        $row5 = mysqli_fetch_array($result5);
        $sql5Ex = "select * from reviewextension where postidx = '$row5[idx]'";
        $result5ex = mysqli_query($connect,$sql5Ex);
        mysqli_data_seek($result5ex, 0);
        $row5ex = mysqli_fetch_array($result5ex);
        echo "{\"listname\":\"clubtable\",\"idx\":\"$row5[idx]\",\"postidx\":\"$row5[postidx]\",\"contents\":\"$row5[contents]\",\"username\":\"$row5[username]\",\"created\":\"$row5[created]\",\"image\":\"$row5[image]\",\"hit\":\"$row5[hit]\",\"spindata1\":\"$row5[spindata1]\",\"spindata2\":\"$row5[spindata2]\",\"postidx\":\"$row5ex[postidx]\",\"text1\":\"$row5ex[product]\",\"text2\":\"$row5ex[wheretobuy]\",\"text3\":\"$row5ex[price]\",\"text4\":\"$row5ex[grade]\",\"text5\":\"$row5ex[etc]\"}";

        if($i<$total_record5){
            echo ",";
        }
    }

    for ($i=0; $i < $total_record6; $i++)
    {
        mysqli_data_seek($result6, $i);
        $row6 = mysqli_fetch_array($result6);
        $sql6Ex = "select * from employmentextension where postidx = '$row6[idx]'";
        $result6ex = mysqli_query($connect,$sql6Ex);
        mysqli_data_seek($result6ex, 0);
        $row6ex = mysqli_fetch_array($result6ex);
        echo "{\"listname\":\"clubtable\",\"idx\":\"$row6[idx]\",\"postidx\":\"$row6[postidx]\",\"contents\":\"$row6[contents]\",\"username\":\"$row6[username]\",\"created\":\"$row6[created]\",\"image\":\"$row6[image]\",\"hit\":\"$row6[hit]\",\"spindata1\":\"$row6[spindata1]\",\"spindata2\":\"$row6[spindata2]\",\"postidx\":\"$row6ex[postidx]\",\"text1\":\"$row6ex[company]\",\"text2\":\"$row6ex[location]\",\"text3\":\"$row6ex[salary]\",\"text4\":\"$row6ex[calendar]\",\"text5\":\"$row6ex[etc]\"}";

        if($i<$total_record6-1){
            echo ",";
        }
    }

    echo "]}";
    mysqli_close($connect);
?>