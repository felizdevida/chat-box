import httpRequest from '../api/httpRequest.js'

export default {

	state: {
		groups: [],
		activeIndex: -1,
	},
	mutations: {
		initGroupStore(state) {
			httpRequest({
				url: '/group/list',
				method: 'get'
			}).then((groups) => {
				this.commit("setGroups",groups);
			})
		},
		setGroups(state,groups){
			state.groups = groups;
		},
		activeGroup(state,index){
			state.activeIndex = index;
		},
		addGroup(state,group){
			state.groups.unshift(group);
		},
		removeGroup(state,groupId){
			state.groups.forEach((g,index)=>{
				if(g.id==groupId){
					state.groups.splice(index, 1);
					if(state.activeIndex  >= state.groups.length){
						state.activeIndex = state.groups.length-1;
					}
				}
			})
			
		},
		updateGroup(state,group){
			state.groups.forEach((g,idx)=>{
				if(g.id==group.id){
					// 拷贝属性
					Object.assign(state.groups[idx], group);
				}
			})
		}
	}	
}