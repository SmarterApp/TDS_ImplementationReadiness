package org.cresst.sb.irp.rest;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.items.ItemAttribute;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.service.ItemService;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Api(name = "Items API", description = "REST API for Items")
@ApiVersion(since = "1.0")
@ApiAuthNone
@Controller
@RequestMapping(value = "/items")
public class ItemController {
	private static Logger logger = Logger.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;

	@ApiMethod(path = "items/item/{id}", description = "Returns application/json structure with list of attriblist, tutorial, statistic, resourceslist, MachineRubric, content, keywordList", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Itemrelease.Item getItem(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id) {
		if (!NumberUtils.isNumber(id)) {
			logger.info("getItem error id is not a number");
		} else {
			return itemService.getItem(Integer.parseInt(id));
		}
		return null;
	}

	@ApiMethod(path = "items/item/{id}/attribute", description = "Returns an item's attribute", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/attribute", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject ItemAttribute getItemAttribute(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id) {
		if (!NumberUtils.isNumber(id)) {
			logger.info("getItemAttribute error id is not a number");
		} else {
			return itemService.getItemAttribute(Integer.parseInt(id));
		}
		return null;
	}

	@ApiMethod(path = "items/item/{id}/attriblist", description = "Returns a list of item attributes", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/attriblist", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Itemrelease.Item.Attriblist getAttriblist(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id) {
		if (!NumberUtils.isNumber(id)) {
			logger.info("getAttriblist error id is not a number");
		} else {
			return itemService.getAttriblist(Integer.parseInt(id));
		}
		return null;

	}

	@ApiMethod(path = "items/item/{id}/attriblist/{attid}", description = "Returns an item attribute's attribute", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/attriblist/{attid}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Itemrelease.Item.Attriblist.Attrib getAttribByIntAttid(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id,
			@ApiParam(name = "attid", description = "Attribute ID", paramType = ApiParamType.PATH)
			@PathVariable("attid") String attid) {
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

	@ApiMethod(path = "items/item/{id}/attriblist2/{attid}", description = "Returns an item attribute's attribute", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/attriblist2/{attid}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Itemrelease.Item.Attriblist.Attrib getAttribByStrAttid(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id,
			@ApiParam(name = "attid", description = "Attribute ID", paramType = ApiParamType.PATH)
			@PathVariable("attid") String attid) {
		if (!NumberUtils.isNumber(id)) {
			logger.info("getAttribByStrAttid error id is not a number");
		} else if (NumberUtils.isNumber(attid)) {
			logger.info("getAttribByStrAttid error attid should like itm_item_xxx and not a number");
		} else {
			return itemService.getAttribByStrAttid(Integer.parseInt(id), attid);
		}
		return null;

	}

	@ApiMethod(path = "items/item/{id}/tutorial", description = "Returns an item tutorial", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/tutorial", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Itemrelease.Item.Tutorial gettutorial(
			@ApiParam(name ="id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id) {
		if (!NumberUtils.isNumber(id)) {
			logger.info("gettutorial error id is not a number");
		} else {
			return itemService.gettutorial(Integer.parseInt(id));
		}
		return null;
	}

	@ApiMethod(path = "items/item/{id}/resourceslist", description = "Returns a resource list", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/resourceslist", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Itemrelease.Item.Resourceslist getResourceslist(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id) {
		if (!NumberUtils.isNumber(id)) {
			logger.info("getResourceslist error id is not a number");
		} else {
			return itemService.getResourceslist(Integer.parseInt(id));
		}
		return null;
	}

	@ApiMethod(path = "items/item/{id}/statistic", description = "Returns an item's statistic", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/statistic", method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject String getStatistic(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id) {
		if (!NumberUtils.isNumber(id)) {
			logger.info("getStatistic error id is not a number");
		} else {
			return itemService.getStatistic(Integer.parseInt(id));
		}
		return null;
	}

	@ApiMethod(path = "items/item/{id}/MachineRubric", description = "Returns an item's MachineRubric", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/MachineRubric", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Itemrelease.Item.MachineRubric getMachineRubric(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id) {
		if (!NumberUtils.isNumber(id)) {
			logger.info("getMachineRubric error id is not a number");
		} else {
			return itemService.getMachineRubric(Integer.parseInt(id));
		}
		return null;

	}

	@ApiMethod(path = "items/item/{id}/RendererSpec", description = "Returns an item's renderer specification", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/RendererSpec", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Itemrelease.Item.RendererSpec getRendererSpec(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id) {
		if (!NumberUtils.isNumber(id)) {
			logger.info("getRendererSpec error id is not a number");
		} else {
			return itemService.getRendererSpec(Integer.parseInt(id));
		}
		return null;
	}

	@ApiMethod(path = "items/item/{id}/gridanswerspace", description = "Returns an item grid answer space", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/gridanswerspace", method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject String getGridanswerspace(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id) {
		if (!NumberUtils.isNumber(id)) {
			logger.info("getGridanswerspace error id is not a number");
		} else {
			return itemService.getGridanswerspace(Integer.parseInt(id));
		}
		return null;
	}

	@ApiMethod(path = "items/item/{id}/content", description = "Returns an item's content", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/content", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<Itemrelease.Item.Content> getContents(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id) {
		if (!NumberUtils.isNumber(id)) {
			logger.info("getContents error id is not a number");
		} else {
			return itemService.getContents(Integer.parseInt(id));
		}
		return null;
		
	}

	@ApiMethod(path = "items/item/{id}/content/{language}", description = "Returns an item's content language", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/content/{language}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject Itemrelease.Item.Content getContent(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id,
			@ApiParam(name = "language", description = "Content's language", paramType = ApiParamType.PATH)
			@PathVariable("language") String language){
		if (!NumberUtils.isNumber(id)) {
			logger.info("getContent error id is not a number");
		} else {
			return itemService.getContent(Integer.parseInt(id), language);
		}
		return null;
	}

	@ApiMethod(path = "items/item/{id}/keywordList", description = "Returns an item's keyword list", verb = ApiVerb.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	@RequestMapping(value = "/item/{id}/keywordList", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public @ApiResponseObject List<Itemrelease.Item.KeywordList> getkeywordList(
			@ApiParam(name = "id", description = "Item ID", paramType = ApiParamType.PATH)
			@PathVariable("id") String id){
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
