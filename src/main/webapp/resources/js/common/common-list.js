/**
 * 
 */
(function(){
	window.submitQuery=function(){
		var param={
			page:$('#page').val(),
			pageSize:$('#pageSize').val()
		};
		submitParams("","GET",param)
	}
})();
$(document).ready(function(){
	$('.data-panel>table').on("click","tbody>tr",function(e){
		$(this).siblings().removeClass("selected");
		$(this).addClass("selected");
	});
	$('gofrist:not(.disabled)').click(function(){
		$('#page').val(1);
		submitQuery();
	});
	$('goprev:not(.disabled)').click(function(){
		$('#page').val($('#page').val()-1);
		submitQuery();
	});
	$('input#jump').change(function(){
		var page=$(this).val();
		page=parseInt(page);
		if (page>0&&page<=parseInt($('#totalPageCount').val())){
			$('#page').val($(this).val());
			submitQuery();
		}else{
			$(this).val($('#page').val());
		}
	});
	$('gonext:not(.disabled)').click(function(){
		$('#page').val(parseInt($('#page').val())+1);
		submitQuery();
	});
	$('golast:not(.disabled)').click(function(){
		$('#page').val($('#totalPageCount').val());
		submitQuery();
	});
});
