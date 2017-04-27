package com.gcit.library.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gcit.library.entity.Author;
import com.gcit.library.entity.Book;
import com.gcit.library.entity.Branch;

public class BranchDAO extends BaseDAO{

	public BranchDAO(Connection conn) {
		super(conn);
	}
	
	public void addBranch(Branch branch) throws ClassNotFoundException, SQLException{
		save("insert into tbl_library_branch (branchName, branchAddress) values (?, ?)", new Object[] {branch.getBranchName(), branch.getBranchAddress()});
	}
	
	public Integer addBranchWithID(Branch branch) throws ClassNotFoundException, SQLException{
		return saveWithID("insert into tbl_library_branch (branchName, branchAddress) values (?, ?)", new Object[] {branch.getBranchName(), branch.getBranchAddress()});
	}
	
	public void updateBranch(Branch branch) throws ClassNotFoundException, SQLException{
		save("update tbl_library_branch set branchName = ?, branchAddress = ? where branchId = ?", new Object[]{branch.getBranchName(), branch.getBranchAddress(), branch.getBranchNo()});
	}
	
	public void deleteBranch(Branch branch) throws ClassNotFoundException, SQLException{
		save("delete * from tbl_library_branch where branchId = ?", new Object[] {branch.getBranchNo()});
	}

	@Override
	public List extractData(ResultSet rs) throws SQLException, ClassNotFoundException {
		BookDAO bdao = new BookDAO(conn);
		List<Branch> branches = new ArrayList<>();
		while(rs.next()){
			Branch b = new Branch();
			b.setBranchNo(rs.getInt("branchId"));
			b.setBranchName(rs.getString("branchName"));
			b.setBranchAddress(rs.getString("branchAddress"));
			b.setStock(bdao.readCopiesFirstLevel(b));
			branches.add(b);
		}
		return branches;
	}

	@Override
	public List extractDataFirstLevel(ResultSet rs) throws SQLException, ClassNotFoundException {
		return extractData(rs);
	}

}