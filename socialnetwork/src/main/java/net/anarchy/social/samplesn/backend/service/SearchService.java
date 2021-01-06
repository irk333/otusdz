package net.anarchy.social.samplesn.backend.service;

import net.anarchy.social.samplesn.backend.common.SearchResult;
import net.anarchy.social.samplesn.backend.dao.SearchDao;
import net.anarchy.social.samplesn.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SearchService {

    @Autowired
    SearchDao searchDao;

    public SearchResult<User> find(String searchText, int pageNo, int pageSize) {
        if (searchText == null || searchText.trim().length() == 0) {
            SearchResult res = new SearchResult();
            res.setPageNo(pageNo);
            res.setPageSize(pageSize);
            res.setRecordCount(0);
            res.setRecords(new ArrayList());
            return res;
        }

        searchText = searchText.trim().toLowerCase();

        SearchResult res = new SearchResult();
        res.setPageNo(pageNo);
        res.setPageSize(pageSize);
        res.setRecordCount(searchDao.countUsers(searchText));
        if (!res.isEmpty()) {
            res.setRecords(searchDao.listUsers(searchText, res.getStart(), res.getPageSize()));
        }
        return res;
    }
}
