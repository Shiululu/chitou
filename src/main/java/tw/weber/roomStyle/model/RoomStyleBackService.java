package tw.weber.roomStyle.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import tw.weber.hotel.model.Hotel;
import tw.weber.room.model.Room;
import tw.weber.room.model.RoomRepository;


@Service
@Transactional
public class RoomStyleBackService {
	
	private String photoFolder = ".//src//main//webapp//WEB-INF//resources//images//weber//roomStyle//";
	
	@Autowired
	private RoomStyleRepository styleRepo;
	
	@Autowired
	private RoomRepository roomRepo;
	
	public List<RoomStyle> findAll(){
		return styleRepo.findAll();
	}
	
	public List<RoomStyle> findAllByHotelID(int hotelID){
		Hotel hotel = new Hotel();
		hotel.setHotelID(hotelID);
		return styleRepo.findAllByHotel(hotel);
	}
	
	public RoomStyle findById(int roomID){
		Optional<RoomStyle> op = styleRepo.findById(roomID);
		return op.get();
	}
	
	public List<RoomStyle> selectByKey(String type,String keyword){
//		if(type.equals("hotelID")) {
//			return rRepo.findAllByHotelIDContaining(keyword);
//		}else if(type.equals("name")) {
//			return rRepo.findAllByNameContaining(keyword);
//		}else if(type.equals("address")) {
//			return rRepo.findAllByAddressContaining(keyword);
//		}else if(type.equals("type")) {
//			return rRepo.findAllByTypeContaining(keyword);
//		}else if(type.equals("intro")) {
//			return rRepo.findAllByIntroContaining(keyword);
//		}else if(type.equals("country")) {
//			return rRepo.findAllByCountryContaining(keyword);
//		}else if(type.equals("phone")) {
//			return rRepo.findAllByPhoneContaining(keyword);
//		}else if(type.equals("status")) {
//			return rRepo.findAllByStatusContaining(keyword);
//		}else if(type.equals("capacity")) {
//			return rRepo.findAllByCapacity(keyword);
//		}else if(type.equals("owner")) {
//			return rRepo.findAllByOwnerContaining(keyword);
//		}else if(type.equals("averagePrice")) {
//			return rRepo.findAllByAveragePriceContaining(keyword);
//		}
		return null;
	}
	
	public RoomStyle insert(RoomStyle style) {
		System.out.println("新增:"+style.getName());
		RoomStyle result = styleRepo.save(style);
		List<Room> roomList = addRooms(result);
		result.setRooms(roomList);
		return result;
	}
	
	public RoomStyle update(RoomStyle style) {
		int roomAmount = style.getRoomAmount();
		
		int styleID = style.getStyleID();
		List<Room> originalRoomList = roomRepo.findAllByStyle(styleID);
		int count = roomAmount - originalRoomList.size();
		if(count > 0) {
			System.out.println("大於0");
			for(int i = 1; i <= count;i++) {
				Room room = new Room();
				room.setStyle(style);
//				originalRoomList.add(room);
				roomRepo.save(room);
			}
		}else if(count < 0) {
			for(Room room:originalRoomList) {
				System.out.println(room.getRoomID());
			}
			System.out.println("小於0");
			for(int i = 0; i < (-count) ; i++) {
				Room room = originalRoomList.get(i);
				roomRepo.delete(room);
			}
			for(Room room:originalRoomList) {
				System.out.println(room.getRoomID());
			}
		}
		return styleRepo.save(style);
	}
	
	public boolean delete(int styleID) {
		styleRepo.deleteById(styleID);
		deletePhotoFolder(styleID);
		return true;
	}
	
	public boolean insertPhoto(int styleID,MultipartFile[] mfiles) throws IllegalStateException, IOException {
		File parentDir = new File(photoFolder + "styleNB" + styleID);
		if(!parentDir.exists()) {
			parentDir.mkdirs();
		}
		int j = 1;
		for(MultipartFile file:mfiles) {
			String searchFileExist = parentDir+"/photo"+j+".jpg";
			File photo = new File(searchFileExist).getAbsoluteFile();
			while(photo.exists()) {
				System.out.println("photo"+j+" is exist");
				j++;
				searchFileExist = parentDir+"/photo"+j+".jpg";
				photo = new File(searchFileExist).getAbsoluteFile();
			}
			file.transferTo(photo);
			j++;
		}

		return true;
	}
	
	public List<Room> addRooms(RoomStyle roomStyle) {
		ArrayList<Room> roomList = new ArrayList<Room>();
		int roomAmount = roomStyle.getRoomAmount();
		for(int i = 0; i < roomAmount ; i++) {
			Room room = new Room();
			room.setStyle(roomStyle);
			roomList.add(room);
		}
		List<Room> resultList = roomRepo.saveAll(roomList);
		return resultList;
	}
	
	public int countPhotos(int styleID) {
		File parentDir = new File(photoFolder + "styleNB" + styleID);
		int i = 0;
		if(parentDir.exists()) {
			File[] files = parentDir.listFiles();
			for(File file:files) {
				i++;
			}
		}
		return i;
	}
	
	public int countRooms(RoomStyle style) {
		return style.getRooms().size();
	}
	
	public boolean deletePhotoFolder(int styleID) {
		File parentDir = new File(photoFolder + "styleNB" + styleID);
		if(parentDir.exists()) {
			File[] files = parentDir.listFiles();
			for(File file:files) {
				System.out.println("刪除檔案:"+file.getName()+"結果"+file.delete());
			}
			return parentDir.delete();
		}
		return false;
	}
}
