$(function() {
   $("a.confirmDeletion").click(function () { 
       let msg = document.getElementById("deleteMsg").value;
       if (!confirm("Are you sure you want to delete \"" + $(this).attr('id')  + "\" "+ $(this).attr('name') +"?\n"+ msg)) {
           return false;
       }
   });
   if($("#content").length){
     ClassicEditor.create(document.querySelector("#content"))
     .catch(error=>{
       console.log(error);
     })
   }
   if($("#description").length){
     ClassicEditor.create(document.querySelector("#description"))
     .catch(error=>{
       console.log(error);
     })
   }
});
/*******************************scroll-to-top button *******************************/ 
//Get the button
let mybutton = document.getElementById("btn-back-to-top");

// When the user scrolls down 20px from the top of the document, show the button
window.onscroll = function () {
  scrollFunction();
};

function scrollFunction() {
  if (
    document.body.scrollTop > 20 ||
    document.documentElement.scrollTop > 20
  ) {
    mybutton.style.display = "block";
  } else {
    mybutton.style.display = "none";
  }
}
// When the user clicks on the button, scroll to the top of the document
mybutton.addEventListener("click", backToTop);

function backToTop() {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}
/********************end of scroll-to-top button ********************/ 
function readURL(input, idNum) {
    if (input.files && input.files[0]) {
        let reader = new FileReader();

        reader.onload = function (e) {
            $("#imgPreview" + idNum).attr("src", e.target.result).width(100).height(100);
        }

        reader.readAsDataURL(input.files[0]);
    }

}

/* Set the width of the sidebar to 250px and the left margin of the page content to 250px */
function openNav() {
  document.getElementById("mySidebar").style.width = "250px";
  document.getElementById("main").style.marginLeft = "250px";
}

/* Set the width of the sidebar to 0 and the left margin of the page content to 0 */
function closeNav() {
  document.getElementById("mySidebar").style.width = "0";
  document.getElementById("main").style.marginLeft = "0";
}