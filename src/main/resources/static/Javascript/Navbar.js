// <script th:inline="javascript">
// var username = [[${user.getUsername()}]];
//
//     console.log("hello");
//     document.getElementById("menu").innerHTML =
//         "<li><a href=\"/homepage\">Home</a></li>"+
//         "<li><a href=\"/maps\">Maps</a></li>"+
//         "<li><a href=\"/rent\">Rent</a></li>"+
//         "<li><a href=\"/addcar\">Add</a></li>"+
//         "<li><a href=\"/request\">Requests</a></li>"+
//         "<li><a href=\"/contact\">Contact</a></li>"+
//         "<li style=\"background-color:transparent;border:none;color:white;font-size:16px\"><a href=\"/MyProfile\">"+username+"</a></li>"+
//         "<li><a href=\"/logout\">logout</a></li>";
// // var role = [[${user.getRole()}]];
// // if (role === "Owner"){
//     // }
//     // else{
//     //     document.getElementById("menu").innerHTML =
//     //         "<li><a href=\"/homepage\">Home</a></li>"+
//     //         "<li><a href=\"/maps\">Maps</a></li>"+
//     //         "<li><a href=\"/rent\">Rent</a></li>"+
//     //         "<li><a href=\"/request\">Requests</a></li>"+
//     //         "<li><a href=\"/contact\">Contact</a></li>"+
//     //         "<li style=\"background-color:transparent;border:none;color:white;font-size:16px\"><a href=\"/MyProfile\">"+username+"</a></li>"+
//     //         "<li><a href=\"/logout\">logout</a></li>";
//     // }
// </script>