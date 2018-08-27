var Member={
		lstMember: null,
		Map: function() {
		    this.key = new Array();
		    this.value = new Object();

		    this.put = function (key, value) {
		        if (this.value[key] == null) {
		            this.key.push(key);
		        }
		        this.value[key] = value;
		    };

		    this.get = function (key) {
		        return this.value[key];
		    };

		    this.remove = function (key) {
		        this.key.remove(key);
		        this.value[key] = null;
		    };

		    this.each = function (fn) {
		        if (typeof fn != 'function') {
		            return;
		        }
		        var len = this.key.length;
		        for (var i = 0; i < len; i++) {
		            var k = this.key[i];
		            fn(k, this.value[k], i);
		        }
		    };

		    this.entrys = function () {
		        var len = this.key.length;
		        var entrys = new Array(len);
		        for (var i = 0; i < len; i++) {
		            entrys[i] = {
		                key: this.key[i],
		                value: this.value[i]
		            };
		        }
		        return entrys;
		    };

		    this.isEmpty = function () {
		        return this.key.length == 0;
		    };

		    this.size = function () {
		        return this.key.length;
		    };
		},
		chooseMember: function(checkbox){
			var id = $(checkbox).val();
			if($(checkbox).is(":checked")){
				Member.lstMember.put(id, Number(id));
			}else{
				Member.lstMember.remove(Number(id));
			}
		},
		addGroup:function(){
			var params = new Object();
			var groupName = $('#groupName').val();
			params.groupName = groupName;
			
			if(Member.lstMember != null && Member.lstMember != undefined){
				var size = Member.lstMember.size();
				var arrMember = new Array();
				for(var i=0;i<size;i++){
					arrMember.push(Member.lstMember.get(Member.lstMember.key[i]));
				}
			}
			params.lstMember = arrMember;
			var kData = $.param(params, true);
			var jsonString=JSON.stringify(params);
			$.ajax({
				type : 'post',
				beforeSend: function(request) {
				    request.setRequestHeader("Content-Type", "application/json");
				  },
				url : '/addgroup',
				data :jsonString,
				dataType : 'json',
				success : function(r) {	
					if(r != null && r.lstMember != null){
						console.log(r.lstMember);
					}
					console.log(r);
					alert('abc');
				},
				error: function(er){
					alert('gruuu');
				}
			});
		},
		searchMember:function(){
			$("#members").html('');
			var params = new Object();
			params.id = "1";
			var kData = $.param(params, true);
			var jsonString=JSON.stringify(params);
			$.ajax({
				type : 'post',
				beforeSend: function(request) {
				    request.setRequestHeader("Content-Type", "application/json");
				  },
				url : '/searchmember',
				data :jsonString,
				success : function(r) {	
					var html = '';
					if(r != null && r.userList != null){
						for(var i=0;i<r.userList.length;i++){ 
							html+='<a href="/chat/'+r.userList[i].id+'?type=member&name='+r.userList[i].username+'&description='+r.userList[i].description+'"><li class="user">'+r.userList[i].username+'</li></a>';
						}
					}
					if(r != null && r.groupList != null){
						for(var i=0;i<r.groupList.length;i++){
							html+='<a href="/chat/'+r.groupList[i].id+'?type=group&name='+r.groupList[i].groupsname+'&description='+r.groupList[i].description+'"><li class="user">'+r.groupList[i].groupsname+'</li></a>';
						}
					}
					$("#members").html(html);
				},
				error: function(er){
					alert('gruuu');
				}
			});
		},
		leaveGroup: function(id){
			var params = new Object();
			params.id = id;
			var kData = $.param(params, true);
			var jsonString=JSON.stringify(params);
	    	$.ajax({
				type : 'post',
				beforeSend: function(request) {
				    request.setRequestHeader("Content-Type", "application/json");
				  },
				url : '/leaveGroup',
				data :jsonString,
				success : function(r) {	
					Member.searchMember();
				},
				error: function(er){
					alert('gruuu');
				}
			});
		}
}