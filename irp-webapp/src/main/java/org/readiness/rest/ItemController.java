package org.readiness.rest;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.readiness.exceptions.NotFoundException;
import org.readiness.items.domain.ItemAttribute;
import org.readiness.items.domain.Itemrelease.Item;
import org.readiness.items.domain.Itemrelease.Item.Attriblist;
import org.readiness.items.domain.Itemrelease.Item.Attriblist.Attrib;
import org.readiness.items.domain.Itemrelease.Item.Content;
import org.readiness.items.domain.Itemrelease.Item.KeywordList;
import org.readiness.items.domain.Itemrelease.Item.MachineRubric;
import org.readiness.items.domain.Itemrelease.Item.RendererSpec;
import org.readiness.items.domain.Itemrelease.Item.Resourceslist;
import org.readiness.items.domain.Itemrelease.Item.Tutorial;
import org.readiness.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
@RequestMapping(value = "/items")
public class ItemController {
	private static Logger logger = Logger.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;

	@RequestMapping(value = "/item/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Item getItem(@PathVariable("id") String id) {
		logger.info("getItem() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getItem error id is not a number");
		} else {
			return itemService.getItem(Integer.parseInt(id));
		}
		return null;
	}

	@RequestMapping(value = "/item/{id}/attribute", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ItemAttribute getItemAttribute(@PathVariable("id") String id) {
		logger.info("getItemAttribute() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getItemAttribute error id is not a number");
		} else {
			return itemService.getItemAttribute(Integer.parseInt(id));
		}
		return null;
	}

	@RequestMapping(value = "/item/{id}/attriblist", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Attriblist getAttriblist(@PathVariable("id") String id) {
		logger.info("getAttriblist() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getAttriblist error id is not a number");
		} else {
			return itemService.getAttriblist(Integer.parseInt(id));
		}
		return null;

	}

	@RequestMapping(value = "/item/{id}/attriblist/{attid}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Attrib getAttribByIntAttid(@PathVariable("id") String id,
			@PathVariable("attid") String attid) {
		logger.info("getAttribByIntAttid() with id " + id + " and attid "
				+ attid);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getAttribByIntAttid error id is not a number");
		} else if (!NumberUtils.isNumber(attid)) {
			logger.info("getAttribByIntAttid error attid is not a number");
		} else {
			return itemService.getAttribByIntAttid(Integer.parseInt(id),
					Integer.parseInt(attid));
		}
		return null;
	}

	@RequestMapping(value = "/item/{id}/attriblist2/{attid}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Attrib getAttribByStrAttid(@PathVariable("id") String id,
			@PathVariable("attid") String attid) {
		logger.info("getAttribByStrAttid() with id " + id + " and attid "
				+ attid);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getAttribByStrAttid error id is not a number");
		} else if (NumberUtils.isNumber(attid)) {
			logger.info("getAttribByStrAttid error attid should like itm_item_xxx and not a number");
		} else {
			return itemService.getAttribByStrAttid(Integer.parseInt(id), attid);
		}
		return null;

	}

	@RequestMapping(value = "/item/{id}/tutorial", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Tutorial gettutorial(@PathVariable("id") String id) {
		logger.info("gettutorial() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("gettutorial error id is not a number");
		} else {
			return itemService.gettutorial(Integer.parseInt(id));
		}
		return null;
	}

	@RequestMapping(value = "/item/{id}/resourceslist", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Resourceslist getResourceslist(@PathVariable("id") String id) {
		logger.info("getResourceslist() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getResourceslist error id is not a number");
		} else {
			return itemService.getResourceslist(Integer.parseInt(id));
		}
		return null;
	}

	@RequestMapping(value = "/item/{id}/statistic", method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getStatistic(@PathVariable("id") String id) {
		logger.info("getStatistic() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getStatistic error id is not a number");
		} else {
			return itemService.getStatistic(Integer.parseInt(id));
		}
		return null;
	}

	@RequestMapping(value = "/item/{id}/MachineRubric", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public MachineRubric getMachineRubric(@PathVariable("id") String id) {
		logger.info("getMachineRubric() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getMachineRubric error id is not a number");
		} else {
			return itemService.getMachineRubric(Integer.parseInt(id));
		}
		return null;

	}

	@RequestMapping(value = "/item/{id}/RendererSpec", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public RendererSpec getRendererSpec(@PathVariable("id") String id) {
		logger.info("getRendererSpec() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getRendererSpec error id is not a number");
		} else {
			return itemService.getRendererSpec(Integer.parseInt(id));
		}
		return null;
	}

	@RequestMapping(value = "/item/{id}/gridanswerspace", method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getGridanswerspace(@PathVariable("id") String id) {
		logger.info("getGridanswerspace() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getGridanswerspace error id is not a number");
		} else {
			return itemService.getGridanswerspace(Integer.parseInt(id));
		}
		return null;
	}

	@RequestMapping(value = "/item/{id}/content", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<Content> getContents(@PathVariable("id") String id) {
		logger.info("getContents() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getContents error id is not a number");
		} else {
			return itemService.getContents(Integer.parseInt(id));
		}
		return null;
		
	}
	
	@RequestMapping(value = "/item/{id}/content/{language}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Content getContent(@PathVariable("id") String id, @PathVariable("language") String language){
		logger.info("getContent() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getContent error id is not a number");
		} else {
			return itemService.getContent(Integer.parseInt(id), language);
		}
		return null;
	}
	
	@RequestMapping(value = "/item/{id}/keywordList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<KeywordList> getkeywordList(@PathVariable("id") String id){
		logger.info("getkeywordList() with id " + id);
		if (!NumberUtils.isNumber(id)) {
			logger.info("getkeywordList error id is not a number");
		} else {
			return itemService.getkeywordList(Integer.parseInt(id));
		}
		return null;
	}
	
	
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */
	/* . . . . . . . . . . . . . EXCEPTION HANDLERS . . . . . . . . . . . . .. */
	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	
	@ExceptionHandler(IndexOutOfBoundsException.class)
	@ResponseBody
	// @ResponseStatus(value = HttpStatus.NOT_FOUND, reason =
	// "IndexOutOfBoundsException")
	public String handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
		logger.info("handleIndexOutOfBoundsException: " + ex.getMessage());
		return ex.getMessage();
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found")
	public String handleNotFoundException(NotFoundException ex) {
		logger.info("NotFoundException: " + ex.getMessage());
		return ex.getMessage();
	}

	@ExceptionHandler(NullPointerException.class)
	@ResponseBody
	public String handleNullPointerException(NullPointerException ex) {
		logger.info("handleNullPointerException: " + ex.getMessage());
		return ex.getMessage();
	}
}
