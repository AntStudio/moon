 $(document).ready(function () {
        $('body').layout({ applyDefaultStyles: true,
        north:{
        	spacing_open :6,
        	spacing_closed:6,
        	minSize:80
        }	
        });
     
        $( "#menu" ).accordion({
        	  heightStyle: "content",
        	  create: function( event, ui ) {
            		  var subMenus="";
            		  $.ajax({
            			  url:contextPath+'/menu/getSubMenus',
            			  data:{
            				  pid:ui.header.attr('id')
            			  },
            				type:'post',
            				dataType:'json',
            				success:function(response){
            					
            					$(eval(response)).each(function(index,e){
            						subMenus+="<li><a href='"+contextPath+e.url+"'>"+e.menuName+"</a></li>";
            						
            					});
            				
            					// $(ui.newPanel).menu("destroy").html(subMenus).menu();
            					$(ui.header.next()).html(subMenus).menu({
            						target:'main'
            					});
            					 
            				},
            				error:function(){
            					alert("error");
            				}
            		  });
        	  },
        	  activate: function( event, ui ) {
        		  if($(ui.newPanel).attr("role")!='menu'){
        		  var subMenus="";
        		  $.ajax({
        			  url:contextPath+'/menu/getSubMenus',
        			  data:{
        				  pid:ui.newHeader.attr('id')
        			  },
        				type:'post',
        				dataType:'json',
        				success:function(response){
        					
        					$(eval(response)).each(function(index,e){
        						subMenus+="<li><a  href='"+contextPath+e.url+"'>"+e.menuName+"</a></li>";
        						
        					});
        					// $(ui.newPanel).menu("destroy").html(subMenus).menu();
        						 $(ui.newPanel).html(subMenus).menu({
             						target:'main'
             					});
        					 
        				},
        				error:function(){
        					alert("error");
        				}
        		  });
        		  }
        	  }
        });
        
        $("#menu").accordion("activate");
       
    });

 