package net.anarchy.social.samplesn.dao;

import net.anarchy.social.samplesn.backend.dao.CityDao;
import net.anarchy.social.samplesn.backend.entity.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Optional;

@SpringBootTest
class CityDaoTests {

	@Autowired
	CityDao cityDao;

	@Test
	void testCRUD() throws Exception {
		// create
		long id = cityDao.insert("Москва");

		// read
		Optional<City> city = cityDao.findById(id);
		Assert.isTrue(city.isPresent(), "1");
		Assert.isTrue(city.get().getName().equals("Москва"), "2");
		Assert.isTrue(city.get().getId() == id, "3");

		// update
		City cityUpdated = cityDao.update(city.get().getId(), "Владивосток");
		Assert.isTrue(cityUpdated.getName().equals("Владивосток"), "4");

		 // delete
		cityDao.delete(id);
		Assert.isTrue(! cityDao.findById(id).isPresent(), "City not deleted");
	}

}
