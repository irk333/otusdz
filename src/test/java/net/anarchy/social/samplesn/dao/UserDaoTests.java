package net.anarchy.social.samplesn.dao;

import net.anarchy.social.samplesn.backend.dao.CityDao;
import net.anarchy.social.samplesn.backend.dao.UserDao;
import net.anarchy.social.samplesn.backend.entity.City;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Optional;

@SpringBootTest
class UserDaoTests {

	@Autowired
	UserDao userDao;

	@Autowired
	CityDao cityDao;

	long cityId1 = 0;

	@BeforeEach
	void setup() {
		cityId1 = cityDao.insert("Москва");
	}

	@AfterEach
	void teardown() {
		cityDao.delete(cityId1);
	}

	@Test
	void testCRUD() throws Exception {
		// create

	}

}
