package net.anarchy.social.samplesn.dao;

import net.anarchy.social.samplesn.backend.dao.InterestDao;
import net.anarchy.social.samplesn.backend.entity.Interest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
class InterestDaoTests {

	@Autowired
	InterestDao interestDao;

//	@Test
	void testCRUD() throws Exception {
		// create
		long id = interestDao.insert("собаки");

		// read
		Optional<Interest> interest = interestDao.findById(id);
		Assert.isTrue(interest.isPresent(), "1");
		Assert.isTrue(interest.get().getName().equals("собаки"), "2");
		Assert.isTrue(interest.get().getId() == id, "3");

		// update
		Interest cityUpdated = interestDao.update(interest.get().getId(), "кошки");
		Assert.isTrue(cityUpdated.getName().equals("кошки"), "4");

		 // delete
		interestDao.delete(id);
		Assert.isTrue(! interestDao.findById(id).isPresent(), "Interest not deleted");
	}

	@Test
	void testFindByNames() throws Exception {
		long id1 = interestDao.insert("собаки");
		long id2 = interestDao.insert("кошки");

		try {
			Set<String> names = new HashSet<>();
			names.add("собаки");

			List<Interest> lst = interestDao.findByNames(names);
			Assert.isTrue(lst.size() == 1, "1");
		} finally {
			interestDao.delete(id1);
			interestDao.delete(id2);
		}
	}

}
