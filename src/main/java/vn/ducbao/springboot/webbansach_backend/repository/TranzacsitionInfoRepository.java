package vn.ducbao.springboot.webbansach_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ducbao.springboot.webbansach_backend.entity.TranzacsitionInfo;

@Repository
public interface TranzacsitionInfoRepository extends JpaRepository<TranzacsitionInfo, Integer> {
}
