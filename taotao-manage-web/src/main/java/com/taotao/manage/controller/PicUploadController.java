package com.taotao.manage.controller;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.manage.vo.PicUploadResult;

@RequestMapping("/pic")
@Controller
public class PicUploadController {
	
	//可以接收的图片类型
	private static final String[] IMAGE_TYPE = {".png",".bmp",".jpg",".gif",".jpeg"};
	
	@Value("${TAOTAO_IMAGE_PATH}")
	private String TAOTAO_IMAGE_PATH;
	
	//是一个处理java对象与json互转的工具类
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 将前端传送的图片保存到fastDFS中并返回图片的路径
	 * produces表示输出的内容类型，就是设置Content-Type的值
	 * @param multipartFile
	 * @return
	 * @throws JsonProcessingException 
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, 
			produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String upload(@RequestParam("uploadFile")MultipartFile multipartFile) throws JsonProcessingException {
		PicUploadResult picUploadResult = new PicUploadResult();
		picUploadResult.setError(1);//非0表示上传失败`
		
		//1、校验文件
		//图片是否合法
		boolean isLegal = false;
		for (String type : IMAGE_TYPE) {
			if(multipartFile.getOriginalFilename().lastIndexOf(type) > 1) {
				isLegal = true;
				break;
			}
		}
		
		if(isLegal) {
			try {
				//校验图片内容是否为图片
				BufferedImage image = ImageIO.read(multipartFile.getInputStream());
				picUploadResult.setHeight(image.getHeight()+"");
				picUploadResult.setWidth(image.getWidth()+"");
				
				//2、上传文件
				//tracker服务器的地址和端口号的文件路径
				String conf_filename = this.getClass().getClassLoader().getResource("tracker.conf").toString();
				if(conf_filename.split(":").length > 2) {
					//在windows中的地址如：file:/D:/itcast/.../tracker.conf
					conf_filename = conf_filename.replaceAll("file:/", "");
				} else {
					//在linux中的地址如：file:/usr/.../tracker.conf
					conf_filename = conf_filename.replaceAll("file:", "");
				}
				//设置全局配置信息
				ClientGlobal.init(conf_filename);
				//创建trackerClient
				TrackerClient trackerClient = new TrackerClient();
				//通过TrackerClient获取trackerServer
				TrackerServer trackerServer = trackerClient.getConnection();
				//创建storageServer
				StorageServer storageServer = null;
				//获取fastDFS的存储对象StorageClient
				StorageClient storageClient = new StorageClient(trackerServer, storageServer);
				
				//文件路径
				String file_ext_name = 
						StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), ".");//文件的拓展名
				
				//上传：参数一：要上传的文件，参数二：文件的拓展名，参数三：文件的其它信息
				String[] upload_file = storageClient.upload_file(multipartFile.getBytes(), file_ext_name, null);
				//解析返回结果
				/**
				 * group1
					M00/00/00/wKgMqFmlLnWAAKqyAAL1OdM3-Qg668.jpg
					http://192.168.12.168/group1/M00/00/00/wKgMqFmlLnWAAKqyAAL1OdM3-Qg668.jpg
				 */
				String url = TAOTAO_IMAGE_PATH;
				for (String str : upload_file) {
					url += "/" + str;
				}
				
				picUploadResult.setError(0);
				picUploadResult.setUrl(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return MAPPER.writeValueAsString(picUploadResult);
	}
}
