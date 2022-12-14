let browserStack = new Array();
$(document).ready(function() {
	loadFromMemory();
	loadFunctionality();
	loadOwnerAndRepos();
	addBackFunctionality();

});
function loadOwnerAndRepos() {
	$("#ownerName").keydown(event => {
		let restClient = new RestCall();
		if (event.which == 13) {
			let ownerName = $("#ownerName").val().trim();
			restClient
				.url('/git/validuser/' + ownerName)
				.httpMethod("GET")
				.fireRestCall((parameters, statusCode, response, responseHeaders) => {
					loadRepos(ownerName);
				});
		}
	});
}
function loadRepos(ownerName) {
	let restClient = new RestCall();
	restClient
		.url('/git/' + ownerName + '/repos')
		.httpMethod("GET")
		.fireRestCall((_parameters, _statusCode, _response, _responseHeaders) => {
			loadRepoList(ownerName, _response)
		});
}
function loadRepoList(ownerName, reposList) {
	var html = "";
	$("#reposList").html(html);
	let isSelected = false;
	reposList.forEach(repo => {
		if (isSelected) {
			html = html + `<option value="${repo.contentUrl}" branch="${repo.branch}">${repo.name}</option>`;
		} else {
			html = html + `<option value="${repo.contentUrl}" branch="${repo.branch}" selected>${repo.name}</option>`;
			isSelected = true;
		}
	})
	$("#reposList").html(html);

	let branch = reposList[0].branch;
	let contentUrl = reposList[0].contentUrl;
	loadBranch(branch)
	loadTableContent({ directUrl: contentUrl + "?ref=" + branch });
	onChangeReposOption();

	localStorage.setItem("reposList", JSON.stringify(reposList));
	localStorage.setItem("ownerName", ownerName);
	loadFunctionality();
}
function onChangeReposOption() {
	$("#reposList").change(event => {
		let branch = $("#reposList").find(":selected").attr("branch");
		loadBranch(branch)
		let contentUrl = $("#reposList").find(":selected").attr("value");
		loadTableContent({ directUrl: contentUrl + "?ref=" + branch });

		browserStack = new Array();

		loadFunctionality();
	});
}
function loadBranch(branch) {
	$("#branch").html(`<option value="${branch}" selected>${branch}</option>`);
}
function loadTableContent(request) {
	updateBrowserStack(request);
	updateCurrentPath(request);
	let restClient = new RestCall();
	restClient.url('/git/content')
		.httpMethod("POST")
		.request(request)
		.fireRestCall((parameters, statusCode, response, responseHeaders) => {
			let html = "";
			response.forEach(row => {
				html = html + createOneTableRow(row);
			})
			$("#tableContents").html(html);
			rowItemOnClick();
		});
}
function createOneTableRow(row) {
	let name = row.name;
	let type = row.type;
	let path = row.path;
	let size = row.size;
	let url = row.url;
	let downloadUrl = row.download_url;
	let htmlUrl = row.html_url;
	if (type == "dir") {
		return `<li>
				<div class="collapsible-header">
					<i class="material-icons">folder</i><div class="oneTableRow" type="${type}" path="${path}" url="${url}" htmlUrl="${htmlUrl}">${name}</div>
				</div>
			</li>`;
	} else {
		return `<li>
				<div class="collapsible-header">
					<i class="material-icons">filter_drama</i><div data-target="modal" class="oneTableRow" type="${type}" path="${path}" url="${url}" downloadurl="${downloadUrl}" htmlurl="${htmlUrl}" size="${size}">${name}</div>
				</div>
			</li>`;
	}
}
function rowItemOnClick() {
	$(".oneTableRow").click((event) => {
		let elem = $(event.target);
		let fileName = elem.html()
		let type = elem.attr("type");
		let path = elem.attr("path");
		let url = elem.attr("url");
		let downloadUrl = elem.attr("downloadurl");
		if (type == "dir") {
			loadTableContent({ directUrl: url, path: path });
		} else {
			loadRawContent(fileName, { rawContentUrl: downloadUrl });

		}
	});
}
function loadRawContent(fileName, request) {
	let restClient = new RestCall();
	restClient.url('/git/raw')
		.httpMethod("POST")
		.request(request)
		.fireRestCall((parameters, statusCode, response, responseHeaders) => {
			let html = `<h5>${fileName}</h5>`;
			response.content.split("  ").join("&emsp;").split(/\n/).map(line => `<div>${line}</div>`).forEach(line => { html = html + line; });
			$("#rawFileContent").html(html);
			M.Modal.getInstance($('.modal')).open();
		});
}
function updateBrowserStack(request) {
	if (browserStack.length == 0 || browserStack[browserStack.length - 1] != request.directUrl) {
		browserStack.push(request.directUrl);
	}
}
function updateCurrentPath(request) {
	let path = getPath(request);
	if (null != path && undefined != path) {
		$("#currentUrlPath").val(path);
		if ("" != path.trim()) {
			$("#currentUrlPath").next("label").addClass("active");
		}
	} else {
		$("#currentUrlPath").next("label").removeClass("active");
	}
}
function getPath(request) {
	if (null == request.path || undefined == request.path) {
		return request.directUrl.split("/contents/")[1].split("?ref=")[0];
	} else {
		return request.path;
	}
}
function loadFromMemory() {
	let ownerName = localStorage.getItem("ownerName");
	if (null != ownerName || undefined != ownerName) {
		$("#ownerName").val(ownerName);
		$("#ownerName").next("label").addClass("active");
		let reposList = localStorage.getItem("reposList");
		if (null != reposList || undefined != reposList) {
			reposList = JSON.parse(reposList);
			loadRepoList(ownerName, reposList)
		} else {
			loadRepos(ownerName);
		}
	}
}
function loadFunctionality() {
	$('select').formSelect();
	$('.chips').chips();
	$('.collapsible').collapsible();
	$(".collapsible-header>.col>.row>.input-field").click(event => event.stopPropagation())
	$(".collapsible-header>.col>.row>.input-field>.input-field").keydown(event => { if (event.which == 13) { event.stopPropagation(); } });
	sortable();
	$('.modal').modal();
}
function addBackFunctionality() {
	$("#back").click((event) => {
		event.stopPropagation();
		if (browserStack.length != 0 && browserStack.length != 1) {
			browserStack.pop();
			let lastUrl = browserStack[browserStack.length - 1];
			loadTableContent({ directUrl: lastUrl });
		}
	});
}
function sortable() {
	$(".one_collapsable").sortable({
		delay: 150,
		stop: function() {
			var selectedData = new Array();
			$(".one_collapsable>li").each(function() {
				selectedData.push($(this).attr("id"));
			});
		}
	});
}
function updateOrder(data) {
	console.log(data);
}