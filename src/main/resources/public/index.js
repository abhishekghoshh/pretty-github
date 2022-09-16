$(document).ready(function() {
	loadFunctionality();
});
function loadFunctionality() {
	$('select').formSelect();
	$('.chips').chips();
	$('.collapsible').collapsible();
	$(".collapsible-header>.col>.row>.input-field").click(event => event.stopPropagation())
	$(".collapsible-header>.col>.row>.input-field>.input-field").keydown(event => {if(event.which==13){ event.stopPropagation();}});
	sortable();
	sortable2();
}
function sortable() {
	$(".row_position").sortable({
		delay: 150,
		stop: function() {
			var selectedData = new Array();
			$(".row_position>tr").each(function() {
				selectedData.push($(this).attr("id"));
			});
			//updateOrder(selectedData);
		}
	});
}
function sortable2() {
	$(".one_collapsable").sortable({
		delay: 150,
		stop: function() {
			var selectedData = new Array();
			$(".one_collapsable>li").each(function() {
				selectedData.push($(this).attr("id"));
			});
			//updateOrder(selectedData);
		}
	});
}
function updateOrder(data) {
	console.log(data);
}